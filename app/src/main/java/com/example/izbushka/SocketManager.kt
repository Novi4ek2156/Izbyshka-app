package com.example.izbushka

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.DataInputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

object SocketManager {
    private var videoSocket: Socket? = null
    private var commandSocket: Socket? = null
    private var videoOutputStream: OutputStream? = null
    private var commandOutputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    @Volatile private var isConnected = false
    @Volatile private var isReceivingVideo = false

    private var currentIpAddress = ""
    private var currentPort = 8001

    private val managerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var videoReceivingJob: Job? = null

    private val _videoFrameFlow = MutableSharedFlow<ByteArray>(replay = 1, extraBufferCapacity = 3)
    val videoFrameFlow = _videoFrameFlow.asSharedFlow()

    private val _videoErrorFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val videoErrorFlow = _videoErrorFlow.asSharedFlow()

    suspend fun connect(ip: String, port: Int = 8001): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            disconnect()
            commandSocket = Socket().apply {
                connect(InetSocketAddress(ip, 5001), 5000)
                commandOutputStream = getOutputStream()
            }
            val newSocket = Socket()
            newSocket.connect(InetSocketAddress(ip, port), 5000)
            newSocket.soTimeout = 10000

            videoSocket = newSocket
            videoOutputStream = newSocket.getOutputStream()
            inputStream = newSocket.getInputStream()

            currentIpAddress = ip
            currentPort = port
            isConnected = true
            true
        } catch (e: Exception) {
            isConnected = false
            false
        }
    }

    suspend fun sendCommand(command: String, port: Int = 5001): Boolean = withContext(Dispatchers.IO) {
        if (!isConnected) return@withContext false
        return@withContext try {
            currentPort = port
            commandOutputStream?.write("$command\n".toByteArray())
            commandOutputStream?.flush()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun startVideoTransmission() {
        if (!isConnected || isReceivingVideo) return
        isReceivingVideo = true
        videoReceivingJob = managerScope.launch {
            sendCommand("CMD_VIDEO_TRANSMISSION", 8001)
            receiveVideoStream()
        }
    }

    private suspend fun receiveVideoStream() {
        val dataInput = DataInputStream(inputStream ?: return)
        try {
            while (isReceivingVideo && isConnected) {
                val frameSize = Integer.reverseBytes(dataInput.readInt())

                if (frameSize <= 0 || frameSize > 3 * 1024 * 1024) continue

                val frameData = ByteArray(frameSize)
                dataInput.readFully(frameData)

                _videoFrameFlow.emit(frameData)
            }
        } catch (e: Exception) {
            if (isReceivingVideo) _videoErrorFlow.emit("Связь прервана")
        } finally {
            isReceivingVideo = false
        }
    }

    fun stopVideoTransmission() {
        isReceivingVideo = false
        videoReceivingJob?.cancel()
        videoReceivingJob = null
    }
    fun stopCommandTransmition() {
        commandOutputStream = null
    }
    fun disconnect() {
        stopVideoTransmission()
        try { videoSocket?.close() } catch (e: Exception) {}
        try { commandSocket?.close() } catch (e: Exception) {}
        videoSocket = null
        commandOutputStream = null
        commandSocket = null
        isConnected = false
    }

    fun isConnected() = isConnected
    fun getCurrentIpAddress() = currentIpAddress
}

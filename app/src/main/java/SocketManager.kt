package com.example.izbushka
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class SocketManager {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null

    // Подключение к серверу (вызывать в Coroutine)
    suspend fun connect(ip: String, port: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            socket = Socket()
            // Таймаут подключения 2 секунды
            socket?.connect(InetSocketAddress(ip, port), 2000)
            outputStream = socket?.getOutputStream()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Отправка команды (как в вашем Python коде)
    suspend fun sendCommand(command: String) = withContext(Dispatchers.IO) {
        try {
            val formattedCommand = "$command\n" // Добавляем перенос строки для Python
            outputStream?.write(formattedCommand.toByteArray())
            outputStream?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        socket?.close()
    }
}
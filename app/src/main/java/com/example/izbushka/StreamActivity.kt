package com.example.izbushka

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var tvStatus: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnBack: Button

    companion object {
        private const val TAG = "VideoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

        // Проверка подключения перед стартом
        if (!SocketManager.isConnected()) {
            Toast.makeText(this, "Нет подключения к серверу!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        initViews()
        setupListeners()
        setupVideoFlow()
        setupErrorFlow()
    }

    private fun initViews() {
        imageView = findViewById(R.id.iv_video)
        tvStatus = findViewById(R.id.tv_status)
        btnStart = findViewById(R.id.btn_start)
        btnStop = findViewById(R.id.btn_stop)
        btnBack = findViewById(R.id.btn_back)

        tvStatus.text = "Статус: Готов"
        btnStop.isEnabled = false
    }

    private fun setupListeners() {
        btnStart.setOnClickListener { startVideo() }
        btnStop.setOnClickListener { stopVideo() }
        btnBack.setOnClickListener {
            // stopVideo() вызовется автоматически в onDestroy
            finish()
        }
    }

    private fun startVideo() {
        tvStatus.text = "Статус: Запуск..."
        btnStart.isEnabled = false
        btnStop.isEnabled = true
        SocketManager.startVideoTransmission()
        Log.d(TAG, "Video start requested")
    }

    private fun stopVideo() {
        SocketManager.stopVideoTransmission()
        tvStatus.text = "Статус: Остановлено"
        btnStart.isEnabled = true
        btnStop.isEnabled = false
        imageView.setImageBitmap(null)
        Log.d(TAG, "Video stopped")
    }

    private fun setupVideoFlow() {
        // repeatOnLifecycle — золотой стандарт:
        // корутина спит, когда приложение свернуто, и просыпается сама
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SocketManager.videoFrameFlow.collectLatest { frameData ->
                    // 1. Декодируем в фоновом потоке (Default), чтобы не фризить UI
                    val bitmap = withContext(Dispatchers.Default) {
                        try {
                            BitmapFactory.decodeByteArray(frameData, 0, frameData.size)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    // 2. Выводим результат в UI (мы уже в Main-потоке благодаря lifecycleScope)
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                        tvStatus.text = "Кадр: ${frameData.size / 1024} KB | ${bitmap.width}x${bitmap.height}"
                    } else {
                        tvStatus.text = "Ошибка декодирования кадра"
                        Log.e(TAG, "❌ BitmapFactory returned null")
                    }
                }
            }
        }
    }

    private fun setupErrorFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SocketManager.videoErrorFlow.collectLatest { errorMessage ->
                    Log.e(TAG, "❌ Ошибка сервера: $errorMessage")
                    Toast.makeText(this@StreamActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    tvStatus.text = "Ошибка: $errorMessage"
                    btnStart.isEnabled = true
                    btnStop.isEnabled = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Важно остановить сетевой поток при выходе с экрана
        SocketManager.stopVideoTransmission()
    }
}

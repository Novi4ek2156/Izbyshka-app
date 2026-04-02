package com.example.izbushka

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.view.MotionEvent
import com.example.izbushka.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class FunctionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_function)

        // Проверяем подключение
        if (!SocketManager.isConnected()) {
            Toast.makeText(this, "Нет подключения к серверу!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        Toast.makeText(
            this,
            "Подключено к ${SocketManager.getCurrentIpAddress()}",
            Toast.LENGTH_SHORT
        ).show()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMenu = findViewById<Button>(R.id.backmain_func)
        btnMenu.setOnClickListener {
            finish()
        }

        val btnLeftHand = findViewById<Button>(R.id.bt_leftarm)
        val btnRightHand = findViewById<Button>(R.id.bt_rightarm)
        val btnBothHands = findViewById<Button>(R.id.bt_wave)

        //btnLeftHand.setOnClickListener {
        //    lifecycleScope.launch {
        //        SocketManager.sendCommand("CMD_MOVE_FORWARD", 5001)
//            }
//        }


        var repeatJob: Job? = null

        @SuppressLint("ClickableViewAccessibility")
        btnLeftHand.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Анимация нажатия
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)

                    // 1. Одиночный запрос (сразу)
                    lifecycleScope.launch {
                        SocketManager.sendCommand("CMD_MOVE_FORWARD", 5001)
                        SocketManager.sendCommand("CMD_MOVE_STOP", 5001)
                    }

                    // 2. Запуск цикла удержания
                    repeatJob?.cancel()
                    repeatJob = lifecycleScope.launch {
                        delay(600) // Ждем 0.4 сек перед началом повторов
                        while (isActive) {
                            SocketManager.sendCommand("CMD_MOVE_FORWARD", 5001)
                            delay(200) // Интервал спама
                        }
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    lifecycleScope.launch { SocketManager.sendCommand("CMD_MOVE_STOP", 5001)
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                        repeatJob?.cancel()
                        repeatJob = null
                    }

                }
            }
            true
        }

        btnRightHand.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_MOVE_BACKWARD", 5001)
            }
        }

        btnBothHands.setOnClickListener {
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_BOTH_HANDS")

            }
        }
    }

    private suspend fun sendCommand(command: String) {
        val result = SocketManager.sendCommand(command)
        if (result) {
            runOnUiThread {
                Toast.makeText(this@FunctionActivity, "Команда отправлена: $command", Toast.LENGTH_SHORT).show()
            }
        } else {
            runOnUiThread {
                Toast.makeText(this@FunctionActivity, "Ошибка отправки команды", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
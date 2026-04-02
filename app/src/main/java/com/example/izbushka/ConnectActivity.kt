package com.example.izbushka

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ConnectActivity : AppCompatActivity() {
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var tvStatus: TextView
    private lateinit var btnConnect: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_connect)

        initViews()
        setupWindowInsets()
        setupListeners()

        // Восстанавливаем состояние при запуске
        updateUI(SocketManager.isConnected())
    }

    private fun setupListeners() {
        btnConnect.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            val ip = textInputLayout.editText?.text.toString().trim()
            if (isValidIp(ip)) {
                connect(ip)
            } else {
                textInputLayout.error = "Неверный IP адрес"
            }
        }

        btnNext.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            if (SocketManager.isConnected()) {
                startActivity(Intent(this, StreamActivity::class.java))
            } else {
                Toast.makeText(this, "Нет подключения к серверу", Toast.LENGTH_SHORT).show()
            }
        }
        val btnBack = findViewById<Button>(R.id.bt_back)
        btnBack.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            finish()
        }
    }

    // ✅ МЕТОД ДЛЯ ПРОВЕРКИ IP АДРЕСА
    private fun isValidIp(ip: String): Boolean {
        return try {
            // Проверяем формат IP адреса
            val pattern = Regex(
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
            )

            if (!pattern.matches(ip)) return false

            // Дополнительная проверка - можно ли распарсить
            val parts = ip.split(".")
            if (parts.size != 4) return false

            parts.forEach { part ->
                val num = part.toIntOrNull() ?: return false
                if (num !in 0..255) return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun connect(ip: String) {
        lifecycleScope.launch {
            btnConnect.isEnabled = false
            tvStatus.text = "Подключение..."
            tvStatus.setTextColor(Color.YELLOW)

            val success = SocketManager.connect(ip, 8001)
            updateUI(success)

            val msg = if (success) "Подключено к $ip" else "Ошибка подключения"
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            btnConnect.isEnabled = true
        }
    }

    private fun updateUI(connected: Boolean) {
        if (connected) {
            tvStatus.text = "✅ Подключено к ${SocketManager.getCurrentIpAddress()}"
            tvStatus.setTextColor(Color.GREEN)
            btnNext.alpha = 1.0f
            btnNext.isEnabled = true
        } else {
            tvStatus.text = "❌ Нет подключения"
            tvStatus.setTextColor(Color.RED)
            btnNext.alpha = 0.5f
            btnNext.isEnabled = false
        }
    }

    private fun initViews() {
        textInputLayout = findViewById(R.id.textInputLayout)
        tvStatus = findViewById(R.id.tv_connection_status)
        btnConnect = findViewById(R.id.bt_connect)
        btnNext = findViewById(R.id.bt_to_functions)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val s = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(s.left, s.top, s.right, s.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI(SocketManager.isConnected())
    }
}
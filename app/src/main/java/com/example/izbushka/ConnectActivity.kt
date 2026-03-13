package com.example.izbushka

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ConnectActivity : AppCompatActivity() {
    private val socketManager = SocketManager()
    private lateinit var textInputLayout: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_connect)
        textInputLayout = findViewById(R.id.textInputLayout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnMenu = findViewById<Button>(R.id.bt_back)

        // Устанавливаем обработчик клика
        btnMenu.setOnClickListener {
            finish()
        }

    }
    fun isValidIp(ip: String): Boolean {
        val ipRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$".toRegex()
        return ip.matches(ipRegex)
    }

    fun ToastMe(view: View) {
        val ip_address = textInputLayout.editText?.text.toString().trim()

        when {
            ip_address.isEmpty() -> {
                textInputLayout.error = "Введите IP-адрес"
            }

            !isValidIp(ip_address) -> {
                textInputLayout.error = "Неверный формат IP"
            }

            else -> {
                textInputLayout.error = null

                // 2. Запускаем подключение в корутине
                lifecycleScope.launch {
                    val isConnected = socketManager.connect(ip_address, 8001) // укажите ваш порт
                    if (isConnected) {
                        Toast.makeText(
                            this@ConnectActivity,
                            "Подключено к $ip_address",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Здесь можно отправить тестовую команду, например:
                        // socketManager.sendCommand("CMD_CONNECT")
                    } else {
                        Toast.makeText(
                            this@ConnectActivity,
                            "Ошибка подключения!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
package com.example.izbushka

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
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

        btnConnect.setOnClickListener {
            val ip = textInputLayout.editText?.text.toString().trim()
            if (Patterns.IP_ADDRESS.matcher(ip).matches()) {
                connect(ip)
            } else {
                textInputLayout.error = "Неверный IP"
            }
        }

        btnNext.setOnClickListener {
            if (SocketManager.isConnected()) {
                startActivity(Intent(this, StreamActivity::class.java))
            } else {
                Toast.makeText(this, "Нет связи", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI(SocketManager.isConnected())
    }

    private fun connect(ip: String) {
        lifecycleScope.launch {
            btnConnect.isEnabled = false
            tvStatus.text = "Подключение..."

            val success = SocketManager.connect(ip)
            updateUI(success)

            val msg = if (success) "Успешно" else "Ошибка"
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            btnConnect.isEnabled = true
        }
    }

    private fun updateUI(connected: Boolean) {
        tvStatus.text = if (connected) "Подключено к ${SocketManager.getCurrentIpAddress()}" else "Нет связи"
        tvStatus.setTextColor(if (connected) Color.GREEN else Color.RED)
        btnNext.alpha = if (connected) 1.0f else 0.5f
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
}

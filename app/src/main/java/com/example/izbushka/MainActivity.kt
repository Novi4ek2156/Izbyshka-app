package com.example.izbushka


import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.zeromq.SocketType
import org.zeromq.ZMQ
import org.zeromq.ZContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим кнопку по ID
        val btnInfo = findViewById<Button>(R.id.bt_info)

        // Устанавливаем обработчик клика
        btnInfo.setOnClickListener {
            // Создаем Intent для перехода на SecondActivity
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent) // Запускаем новую Activity
        }
        val btnFunc = findViewById<Button>(R.id.bt_func)
        btnFunc.setOnClickListener {
            val intent = Intent(this, FunctionActivity::class.java)
            startActivity(intent)
        }

        val btnConnect = findViewById<Button>(R.id.bt_connect)
        btnConnect.setOnClickListener{
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
        }
    }
    fun Innactive(view: View) {
        Toast.makeText(applicationContext, "Временно не работает(", Toast.LENGTH_SHORT).show()

    }
}
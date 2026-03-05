package com.example.izbushka

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим кнопку по ID
        val btnInfo = findViewById<Button>(R.id.button)

        // Устанавливаем обработчик клика
        btnInfo.setOnClickListener {
            // Создаем Intent для перехода на SecondActivity
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent) // Запускаем новую Activity
        }
        val btnFunc = findViewById<Button>(R.id.button6)
        btnFunc.setOnClickListener {
            val intent = Intent(this, FunctionActivity::class.java)
            startActivity(intent)
        }
    }
}
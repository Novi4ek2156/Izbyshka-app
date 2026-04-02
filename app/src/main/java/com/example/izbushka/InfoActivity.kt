package com.example.izbushka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnMenu = findViewById<Button>(R.id.button7)

        // Устанавливаем обработчик клика
        btnMenu.setOnClickListener {
            finish()
        }
        val btnFunc1 = findViewById<Button>(R.id.button4)
        btnFunc1.setOnClickListener {
            it.animate()
                .scaleX(0.6f)
                .scaleY(0.6f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
        }
    }

}
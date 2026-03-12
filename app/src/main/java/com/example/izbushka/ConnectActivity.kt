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

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_connect)
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
        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        val text = textInputLayout.editText?.text.toString()

    }
    fun ToastMe(view: View) {
        Toast.makeText(applicationContext, "Успешно!", Toast.LENGTH_SHORT).show()
    }
}
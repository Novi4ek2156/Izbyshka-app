package com.example.izbushka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FunctionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_function)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //кнопки функций
        val btnWave = findViewById<Button>(R.id.bt_wave)
        val btnOnMusic = findViewById<Button>(R.id.bt_onmusic)
        val btnOffMusic = findViewById<Button>(R.id.bt_offmusic)
        val btnLeftHand = findViewById<Button>(R.id.bt_leftarm)
        val btnRightHand = findViewById<Button>(R.id.bt_rightarm)
        val btnLookUp = findViewById<Button>(R.id.bt_up)
        val btnLookDown = findViewById<Button>(R.id.bt_down)
        //кнопки перехода между окнами
        val btnMenu = findViewById<Button>(R.id.backmain_func)
        val btnInfo = findViewById<Button>(R.id.bt_info)
        val btnConn = findViewById<Button>(R.id.bt_connect)
        val btnStream = findViewById<Button>(R.id.bt_stream)
        //переход между окнами
        btnMenu.setOnClickListener {
            finish()
        }
        btnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
        btnConn.setOnClickListener {
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
        }
        btnStream.setOnClickListener {
            val intent = Intent(this, StreamActivity::class.java)
            startActivity(intent)
        }
        //Команда помахать
        btnWave.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_HANDS_UP", 5001)
            }

        }
        //вкл музыку
        btnOnMusic.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_MUSIC_PLAY", 5001)
            }
        }
        // выкл музыку
        btnOffMusic.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_MUSIC_PAUSE", 5001)
            }
        }
        //сжать левую руку
        btnLeftHand.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_CLENCH_LEFT", 5001)
            }
        }
        //сжать правую руку
        btnRightHand.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_CLENCH_RIGHT", 5001)
            }
        }
        //посмотреть вверх
        btnLookUp.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_LOOK_UP", 5001)
            }
        }
        //посмотреть вниз
        btnLookDown.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_LOOK_DOWN", 5001)
            }
        }
    }

}
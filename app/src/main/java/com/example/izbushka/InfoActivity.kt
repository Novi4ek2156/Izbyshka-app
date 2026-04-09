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
        //кнопки меню
        val btnMenu = findViewById<Button>(R.id.button7)
        val btnStream = findViewById<Button>(R.id.bt_stream)
        val btnConnect = findViewById<Button>(R.id.bt_connect)
        val btnFunction = findViewById<Button>(R.id.bt_func)
        //кнопки функций
        val btnOnAboutMe = findViewById<Button>(R.id.btn_on_yrs)
        val btnOffAboutMe = findViewById<Button>(R.id.btn_off_yrs)
        val btnOnRadio = findViewById<Button>(R.id.btn_radio_on)
        val btnOffRadio = findViewById<Button>(R.id.btn_radio_off)
        val btnAboutCode = findViewById<Button>(R.id.btn_inf_code)
        val btnAboutPlats = findViewById<Button>(R.id.btn_inf_card)
        val btnInfPower = findViewById<Button>(R.id.btn_inf_power)
        val btnAboutII = findViewById<Button>(R.id.btn_info_ai)
        // Переход между activity
        btnMenu.setOnClickListener {
            finish()
        }
        btnFunction.setOnClickListener {
            val intent = Intent(this, FunctionActivity::class.java)
            startActivity(intent)
        }
        btnStream.setOnClickListener {
            val intent = Intent(this, StreamActivity::class.java)
            startActivity(intent)
        }
        btnConnect.setOnClickListener {
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
        }
        //эффекты кнопок и отправка команд
        btnOnAboutMe.setOnClickListener {
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
        btnOffAboutMe.setOnClickListener {
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
        btnOnRadio.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_RADIO_ON", 5001)
            }
        }
        btnOffRadio.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            lifecycleScope.launch {
                SocketManager.sendCommand("CMD_RADIO_OFF", 5001)
            }
        }
        btnAboutCode.setOnClickListener {
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
        btnAboutPlats.setOnClickListener {
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
        btnInfPower.setOnClickListener {
            it.animate()
                .scaleX(0.7f)
                .scaleY(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).duration = 100
                }
        }
        btnAboutII.setOnClickListener {
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
    }

}
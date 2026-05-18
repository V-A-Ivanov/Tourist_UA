package com.example.touristua

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class SuggestActivity : AppCompatActivity() {

    private val SCRIPT_URL = "https://script.google.com/macros/s/AKfycbw_aLjgQRvoxnFHTZwKfRAPbJkRl_EiL8B7_-JhdhPvL6cNbKbdNuQURya0gbFgN0g/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val controller = androidx.core.view.WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Кнопка назад
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        //типи пам'яток
        val spinner = findViewById<Spinner>(R.id.spinnerType)
        val types = listOf("Замок", "Собор", "Фортеця", "Музей", "Природна пам'ятка", "Парк", "Інше")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Кнопка відправити
        findViewById<Button>(R.id.btnSend).setOnClickListener {
            val type = spinner.selectedItem.toString()
            val region = findViewById<EditText>(R.id.etRegion).text.toString().trim()
            val description = findViewById<EditText>(R.id.etDescription).text.toString().trim()

            if (region.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendToSheets(type, region, description)
        }
    }

    private fun sendToSheets(type: String, region: String, description: String) {
        val btnSend = findViewById<Button>(R.id.btnSend)
        btnSend.isEnabled = false
        btnSend.text = "Надсилаємо..."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encodedType = java.net.URLEncoder.encode(type, "UTF-8")
                val encodedRegion = java.net.URLEncoder.encode(region, "UTF-8")
                val encodedDesc = java.net.URLEncoder.encode(description, "UTF-8")

                val url = "$SCRIPT_URL?type=$encodedType&region=$encodedRegion&description=$encodedDesc"
                val response = URL(url).readText()

                withContext(Dispatchers.Main) {
                    if (response.contains("success")) {
                        Toast.makeText(
                            this@SuggestActivity,
                            "✅ Дякуємо! Пропозицію надіслано",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@SuggestActivity,
                            "Помилка. Спробуйте ще раз",
                            Toast.LENGTH_SHORT
                        ).show()
                        btnSend.isEnabled = true
                        btnSend.text = "📩 Надіслати"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SuggestActivity,
                        "Помилка мережі: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnSend.isEnabled = true
                    btnSend.text = "📩 Надіслати"
                }
            }
        }
    }
}

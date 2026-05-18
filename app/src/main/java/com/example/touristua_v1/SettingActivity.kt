package com.example.touristua

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val controller = androidx.core.view.WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val btnLight = findViewById<Button>(R.id.btnThemeLight)
        val btnDark = findViewById<Button>(R.id.btnThemeDark)

        updateThemeButtons(isDarkMode, btnLight, btnDark)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // зміна теми
        btnLight.setOnClickListener {
            sharedPref.edit().putBoolean("dark_mode", false).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            recreate()
        }

        btnDark.setOnClickListener {
            sharedPref.edit().putBoolean("dark_mode", true).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            recreate()
        }
            //нові функції?

        // запропонувати пам'ятку
        findViewById<Button>(R.id.btnSuggestLandmark).setOnClickListener {
            val intent = Intent(this, SuggestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateThemeButtons(isDark: Boolean, btnLight: Button, btnDark: Button) {
        if (isDark) {
            btnDark.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF005BBB.toInt())
            btnDark.setTextColor(0xFFFFFFFF.toInt())
            btnLight.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE0E0E0.toInt())
            btnLight.setTextColor(0xFF1A1A1A.toInt())
        } else {
            btnLight.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF005BBB.toInt())
            btnLight.setTextColor(0xFFFFFFFF.toInt())
            btnDark.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE0E0E0.toInt())
            btnDark.setTextColor(0xFF1A1A1A.toInt())
        }
    }

    private fun updateLangButtons(isEn: Boolean, btnUa: Button, btnEn: Button) {
        if (isEn) {
            btnEn.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF005BBB.toInt())
            btnEn.setTextColor(0xFFFFFFFF.toInt())
            btnUa.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE0E0E0.toInt())
            btnUa.setTextColor(0xFF1A1A1A.toInt())
        } else {
            btnUa.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF005BBB.toInt())
            btnUa.setTextColor(0xFFFFFFFF.toInt())
            btnEn.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE0E0E0.toInt())
            btnEn.setTextColor(0xFF1A1A1A.toInt())
        }
    }

    private fun updateViewButtons(isCompact: Boolean, btnCards: Button, btnCompact: Button) {
        if (isCompact) {
            btnCompact.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF005BBB.toInt())
            btnCompact.setTextColor(0xFFFFFFFF.toInt())
            btnCards.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE0E0E0.toInt())
            btnCards.setTextColor(0xFF1A1A1A.toInt())
        } else {
            btnCards.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF005BBB.toInt())
            btnCards.setTextColor(0xFFFFFFFF.toInt())
            btnCompact.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE0E0E0.toInt())
            btnCompact.setTextColor(0xFF1A1A1A.toInt())
        }
    }
}
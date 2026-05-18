package com.example.touristua

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Glide.get(this).clearMemory()

        val controller = androidx.core.view.WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val location = intent.getSerializableExtra("location") as? Location

        if (location == null) {
            Toast.makeText(this, "Помилка: дані не передані", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.tvDetailName).text = location.name
        findViewById<TextView>(R.id.tvDetailCity).text = "📍 ${location.region}"
        findViewById<TextView>(R.id.tvDetailType).text = location.type
        findViewById<TextView>(R.id.tvDetailYear).text = "${location.year} р."
        findViewById<TextView>(R.id.tvDetailRegion).text = location.region
        findViewById<TextView>(R.id.tvDetailDescription).text = location.description

        // Кнопка назад
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // допоміжні функції в майбутньому?

        // фото
        val imageView = findViewById<ImageView>(R.id.ivDetailImage)
        val tvImageError = findViewById<TextView>(R.id.tvImageError)

        imageView.setOnClickListener {
            val url = location.imageUrl
            if (url.isNotEmpty() && url.startsWith("http")) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } else {
                Toast.makeText(this, "Посилання недоступне", Toast.LENGTH_SHORT).show()
            }
        }

        if (location.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(location.imageUrl)
                .placeholder(R.drawable.ic_image_error)
                .error(R.drawable.ic_image_error)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(800, 600)
                .centerCrop()
                .listener(object : com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable> {
                    override fun onLoadFailed(
                        e: com.bumptech.glide.load.engine.GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        android.util.Log.e("GlideError", "Помилка завантаження: ${location.imageUrl}", e)
                        tvImageError.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: android.graphics.drawable.Drawable,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        android.util.Log.d("GlideOK", "Фото завантажено: ${location.imageUrl}")
                        tvImageError.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_image_error)
            tvImageError.visibility = View.VISIBLE
        }
    }

    private fun shareLocation(location: Location) {
        val text = "🏛 ${location.name}\n📍 ${location.region}\n\n${location.description}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, "Поділитися через..."))
    }
}
package com.example.touristua

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private val locationList = ArrayList<Location>()
    private var isCompact = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        isCompact = sharedPref.getBoolean("compact_view", true)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controller = androidx.core.view.WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        dbHelper = DatabaseHelper(this)

        if (dbHelper.getAllLocations().isEmpty()) {
            addSampleData()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        refreshList()

        findViewById<android.widget.ImageButton>(R.id.btnSettings).setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextInputEditText>(R.id.etSearch).addTextChangedListener(object :
            android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = if (s.isNullOrEmpty()) {
                    dbHelper.getAllLocations()
                } else {
                    dbHelper.searchLocations(s.toString())
                }
                updateList(filtered)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        isCompact = sharedPref.getBoolean("compact_view", true)
        refreshList()
    }
    private fun refreshList() {
        val all = dbHelper.getAllLocations()
        locationList.clear()
        locationList.addAll(all)
        updateList(all)
    }

    private fun updateList(list: ArrayList<Location>) {
        recyclerView.adapter = SimpleLocationAdapter(list, isCompact) { loc ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("location", loc)
            startActivity(intent)
        }
        findViewById<android.widget.TextView>(R.id.tvCount).text = list.size.toString()
    }

    private fun addSampleData() {
        try {
            val jsonString = assets.open("locations.json").bufferedReader().use { it.readText() }
            val jsonArray = org.json.JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val loc = Location().apply {
                    name = obj.getString("name")
                    region = obj.getString("region")
                    type = obj.getString("type")
                    year = obj.getInt("year")
                    description = obj.getString("description")
                    imageUrl = obj.optString("imageUrl", "")
                }
                dbHelper.addLocation(loc)
            }
            refreshList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
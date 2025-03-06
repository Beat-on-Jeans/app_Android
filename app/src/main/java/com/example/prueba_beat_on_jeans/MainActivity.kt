package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val button: Button = findViewById(R.id.login_button)

        val savedValue = getBooleanFromPreferences(this, "my_boolean_key")

        if (savedValue) {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            saveBooleanToPreferences(this, "my_boolean_key", true)
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    fun saveBooleanToPreferences(context: Context, key: String, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanFromPreferences(context: Context, key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }
}

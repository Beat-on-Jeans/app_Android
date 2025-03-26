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

        UserSession.loadUserData(this)

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

    object UserSession {
        var id: Int? = null
        var username: String? = null
        var email: String? = null
        var password: String? = null
        var rolId: Int? = null
        var urlImg: Int? = null
        var isLoggedIn: Boolean = false

        fun setUserData(context: Context, id: Int, username: String, email: String,
                        password: String, rolId: Int, urlImg: Int) {
            this.id = id
            this.username = username
            this.email = email
            this.password = password
            this.rolId = rolId
            this.urlImg = urlImg
            this.isLoggedIn = true

            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("id", id)
                putString("username", username)
                putString("email", email)
                putString("password", password)
                putInt("rolId", rolId)
                putInt("urlImg", urlImg)
                putBoolean("isLoggedIn", true)
                apply()
            }
        }

        fun loadUserData(context: Context) {
            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            id = sharedPref.getInt("userId", 0)
            username = sharedPref.getString("username", null)
            email = sharedPref.getString("email", null)
            password = sharedPref.getString("password", null)
            rolId = sharedPref.getInt("rolId", 0)
            urlImg = sharedPref.getInt("urlImg", 0)
            isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        }

        fun clearSession(context: Context) {
            id = 0
            username = null
            email = null
            password = null
            rolId = 0
            urlImg = 0
            isLoggedIn = false

            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                clear()
                apply()
            }
        }
    }

}

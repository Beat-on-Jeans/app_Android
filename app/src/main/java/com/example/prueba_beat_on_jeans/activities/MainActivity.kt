package com.example.prueba_beat_on_jeans.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba_beat_on_jeans.R
import java.util.Locale


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val button: Button = findViewById(R.id.login_button)

        setLanguaje()

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

    private fun setLanguaje() {
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString("language", Locale.getDefault().language)
        if (selectedLanguage != null) {
            changelanguaje(selectedLanguage)
        }
    }

    private fun changelanguaje(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        saveLanguage(language)
    }

    private fun saveLanguage(language: String) {
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_language", language)
        editor.apply()
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
        var location: String? = null
        var urlImg: String? = null
        var isLoggedIn: Boolean = false

        fun setUserData(context: Context, id: Int, username: String, email: String,
                        password: String, rolId: Int, location: String, urlImg: String) {
            UserSession.id = id
            UserSession.username = username
            UserSession.email = email
            UserSession.password = password
            UserSession.rolId = rolId
            UserSession.location = location
            UserSession.urlImg = urlImg
            isLoggedIn = true

            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("id", id)
                putString("username", username)
                putString("email", email)
                putString("password", password)
                putInt("rolId", rolId)
                putString("location", location)
                putString("urlImg", urlImg)
                putBoolean("isLoggedIn", true)
                apply()
            }
        }

        fun userLogin(context: Context, id: Int, rolId: Int, email: String,
                        password: String) {
            UserSession.id = id
            UserSession.rolId = rolId
            UserSession.email = email
            UserSession.password = password
            isLoggedIn = true

            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("id", id)
                putInt("rolId", rolId)
                putString("email", email)
                putString("password", password)
                putBoolean("isLoggedIn", true)
                apply()
            }
        }

        fun loadUserData(context: Context) {
            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            id = sharedPref.getInt("id", 0)
            email = sharedPref.getString("email", null)
            password = sharedPref.getString("password", null)
            rolId = sharedPref.getInt("rolId", 0)

        }

        fun clearSession(context: Context) {
            id = 0
            username = null
            email = null
            password = null
            rolId = 0
            location = null
            urlImg = null
            isLoggedIn = false

            val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                clear()
                apply()
            }
        }
    }

}
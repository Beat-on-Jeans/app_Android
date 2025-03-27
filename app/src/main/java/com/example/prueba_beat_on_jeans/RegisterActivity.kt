package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val buttonMusician: Button = findViewById(R.id.musician_button)
        val buttonLocal: Button = findViewById(R.id.local_button)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imagebuttonBack: ImageButton = findViewById(R.id.back_imageButton)

        buttonContinue.isEnabled = false
        buttonContinue.alpha = 0.5f

        var rol = 0

        buttonMusician.setOnClickListener{
            rol = 1
            buttonContinue.isEnabled = true
            buttonContinue.alpha = 1f
            buttonMusician.isEnabled = false
            buttonMusician.alpha = 0.8f
            buttonLocal.isEnabled = true
            buttonLocal.alpha = 1f
        }

        buttonLocal.setOnClickListener{
            rol = 2
            buttonContinue.isEnabled = true
            buttonContinue.alpha = 1f
            buttonLocal.isEnabled = false
            buttonLocal.alpha = 0.2f
            buttonMusician.isEnabled = true
            buttonMusician.alpha = 1f
        }

        buttonContinue.setOnClickListener {
            MainActivity.UserSession.rolId = rol
            val intent = Intent(this, RegisterActivity2::class.java)
            startActivity(intent)
        }

        imagebuttonBack.setOnClickListener{
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}
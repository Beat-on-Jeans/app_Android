package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val button: Button = findViewById(R.id.login_button)
        val editTextUser: EditText = findViewById(R.id.user_editText)
        val editTextPassword: EditText = findViewById(R.id.password_editText)
        val registerText: TextView = findViewById(R.id.register_text)

        button.setOnClickListener {
            if (editTextUser.text.toString() == "admin" && editTextPassword.text.toString() == "1234"){
                val intent = Intent(this, NavigationBar::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "User or password incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
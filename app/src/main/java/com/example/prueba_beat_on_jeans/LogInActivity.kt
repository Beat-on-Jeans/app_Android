package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val loginButton: Button = findViewById(R.id.login_button)
        val editTextUsername: EditText = findViewById(R.id.user_editText)
        val editTextPassword: EditText = findViewById(R.id.password_editText)
        val registerText: TextView = findViewById(R.id.register_text)

        // Login button click listener
        loginButton.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Check if username and password fields are not empty
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Call method to verify the user
                verifyUser(username, password)
            } else {
                Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Register Text click listener to open RegisterActivity
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Method to verify if user exists with matching username and password
    private fun verifyUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                // Fetch users from API
                val users = RetrofitClient.instance.getUsers()

                // Find if a user exists with the entered username and password
                val validUser = users.find { it.email == username && it.password == password }

                if (validUser != null) {
                    // If valid user, navigate to HomeActivity
                    val intent = Intent(this@LogInActivity, FIrstFragment::class.java)
                    startActivity(intent)
                    finish() // Close current activity to avoid returning to login screen
                } else {
                    // If no match found, show error message
                    Toast.makeText(this@LogInActivity, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error connecting to the API", e)
                Toast.makeText(this@LogInActivity, "Error connecting to the API", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

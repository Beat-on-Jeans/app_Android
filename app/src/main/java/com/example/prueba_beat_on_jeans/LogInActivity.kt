package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.content.Intent
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prueba_beat_on_jeans.MainActivity.UserSession
import kotlinx.coroutines.launch
import java.io.IOException

class LogInActivity : AppCompatActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        //comproveUser()

        val loginButton: Button = findViewById(R.id.login_button)
        val editTextUsername: EditText = findViewById(R.id.user_editText)
        val editTextPassword: EditText = findViewById(R.id.password_editText)
        val registerText: TextView = findViewById(R.id.register_text)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Check if username and password fields are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call method to verify the user
                verifyUser(email, password)
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

    private fun comproveUser() {
        if(MainActivity.UserSession.isLoggedIn){
            val intent = Intent(this@LogInActivity, NavigationBar::class.java)
            startActivity(intent)
        }
    }

    private fun verifyUser(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val users = RetrofitClient.instance.getUsers()
                val validUser = users.find { user ->
                    user.correo == email && user.contrasena == password && (user.rolId == 1 || user.rolId == 2)
                }
                if (validUser != null) {
                    MainActivity.UserSession.clearSession(this@LogInActivity)
                    MainActivity.UserSession.setUserData(this@LogInActivity,
                        validUser.id,validUser.nombre,validUser.correo,validUser.contrasena,validUser.rolId,R.drawable.hugo)
                    val intent = Intent(this@LogInActivity, NavigationBar::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LogInActivity,
                        "Usuario o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(
                    this@LogInActivity,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

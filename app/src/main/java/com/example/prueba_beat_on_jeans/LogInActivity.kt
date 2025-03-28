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
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    private fun verifyUser(email: String, password: String) {
        lifecycleScope.launch {
                val user = User(
                    0,
                    "",
                    email,
                    password,
                    0,
                    "",
                    ""
                )
                RetrofitClient.instance.loginUser(user).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val userRecibido = response.body()
                            if (userRecibido != null) {
                                MainActivity.UserSession.urlImg = userRecibido.imagen
                                MainActivity.UserSession.email = userRecibido.correo
                                MainActivity.UserSession.password = userRecibido.contrasena
                                MainActivity.UserSession.username = userRecibido.nombre
                                MainActivity.UserSession.location = userRecibido.ubicacion
                                MainActivity.UserSession.id = userRecibido.id
                                MainActivity.UserSession.rolId = userRecibido.rolId
                                MainActivity.UserSession.isLoggedIn = true
                                val intent = Intent(this@LogInActivity, NavigationBar::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(applicationContext, "Correo o contraseña incorrectos.", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

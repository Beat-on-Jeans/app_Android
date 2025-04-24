package com.example.prueba_beat_on_jeans.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prueba_beat_on_jeans.classes.NavigationBar
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.User
import kotlinx.coroutines.launch
import org.bouncycastle.crypto.engines.BlowfishEngine
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.params.KeyParameter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    private val secretKey = "999a999ale469993"

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        comproveUser()

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
                verifyUser(username, password) //encryptPassword(password)
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
            val intent = Intent(this, NavigationBar::class.java)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encryptPassword(pswd: String): String {

        val engine = BlowfishEngine()
        val blockCipher = PaddedBufferedBlockCipher(engine)

        val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
        blockCipher.init(true, KeyParameter(keyBytes))

        val inputBytes = pswd.toByteArray(Charsets.UTF_8)
        val outputBytes = ByteArray(blockCipher.getOutputSize(inputBytes.size))

        var length = blockCipher.processBytes(inputBytes, 0, inputBytes.size, outputBytes, 0)
        length += blockCipher.doFinal(outputBytes, length)

        return java.util.Base64.getEncoder().encodeToString(outputBytes.copyOf(length))
    }
}

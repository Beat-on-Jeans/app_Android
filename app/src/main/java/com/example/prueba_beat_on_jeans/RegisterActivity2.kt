package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var userTemp: UserTemp

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2)

        userTemp = intent.getParcelableExtra("USER_TEMP") ?: UserTemp()

        val editTextMail: EditText = findViewById(R.id.mail_text)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imagebuttonBack: ImageButton = findViewById(R.id.back_imageButton)

        buttonContinue.setOnClickListener {
            if(editTextMail.text.isEmpty()){
                Toast.makeText(this, "Escribe tu correo", Toast.LENGTH_SHORT).show()
            } else {
                verifyUser(editTextMail.text.toString())
            }
        }

        imagebuttonBack.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("USER_TEMP", userTemp)
            startActivity(intent)
        }
    }

    private fun verifyUser(email: String) {
        lifecycleScope.launch {
            try {
                val users = RetrofitClient.instance.getUsers()
                val userFound = users.find { user -> user.correo == email }

                if (userFound != null) {
                    Toast.makeText(
                        this@RegisterActivity2,
                        "Este correo ya se está usando en otra cuenta",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    userTemp.correo = email
                    val intent = Intent(this@RegisterActivity2, RegisterActivity3::class.java)
                    intent.putExtra("USER_TEMP", userTemp)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(
                    this@RegisterActivity2,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
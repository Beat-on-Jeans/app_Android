package com.example.prueba_beat_on_jeans.activities

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
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import kotlinx.coroutines.launch

class RegisterActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2)

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
                    MainActivity.UserSession.email = email
                    val intent = Intent(this@RegisterActivity2, RegisterActivity3::class.java)
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
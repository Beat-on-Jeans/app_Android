package com.example.prueba_beat_on_jeans

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity3 : AppCompatActivity() {
    private lateinit var userTemp: UserTemp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register3)

        userTemp = intent.getParcelableExtra("USER_TEMP") ?: UserTemp()

        val editTextPassword1: EditText = findViewById(R.id.password1_text)
        val editTextPassword2: EditText = findViewById(R.id.password2_text)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imagebuttonBack: ImageButton = findViewById(R.id.back_imageButton)

        buttonContinue.setOnClickListener {
            if(editTextPassword1.text.toString() != editTextPassword2.text.toString()) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else if (editTextPassword1.text.toString() == "" && editTextPassword2.text.toString() == "") {
                Toast.makeText(this, "Error: Contraseña vacía", Toast.LENGTH_SHORT).show()
            } else {
                userTemp.contrasena = editTextPassword1.text.toString()
                val intent = Intent(this, RegisterActivity4::class.java)
                intent.putExtra("USER_TEMP", userTemp)
                startActivity(intent)
            }
        }

        imagebuttonBack.setOnClickListener{
            val intent = Intent(this, RegisterActivity2::class.java)
            intent.putExtra("USER_TEMP", userTemp)
            startActivity(intent)
        }
    }
}
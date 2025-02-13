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

class RegisterActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register4)

        val editTextName: EditText = findViewById(R.id.name_text)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imagebuttonBack: ImageButton = findViewById(R.id.back_imageButton)

        buttonContinue.setOnClickListener {
            if(editTextName.text.isEmpty()){
                Toast.makeText(this, "Escribe tu nombre", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, RegisterActivity5::class.java)
                startActivity(intent)
            }
        }

        imagebuttonBack.setOnClickListener{
            val intent = Intent(this, RegisterActivity3::class.java)
            startActivity(intent)
        }

    }
}
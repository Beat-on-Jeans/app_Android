package com.example.prueba_beat_on_jeans.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba_beat_on_jeans.R

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val btnReturn = findViewById<ImageButton>(R.id.BtnReturn)

        btnReturn.setOnClickListener{
            finish()
        }

        val ediTxtlocation = findViewById<EditText>(R.id.Location)

        ediTxtlocation.setOnClickListener{

        }

    }
}
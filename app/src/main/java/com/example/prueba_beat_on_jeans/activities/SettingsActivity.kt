package com.example.prueba_beat_on_jeans.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.UserUpdated
import com.example.prueba_beat_on_jeans.classes.CreateTicketDialog
import com.example.prueba_beat_on_jeans.classes.NavigationBar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val btnReturn = findViewById<ImageButton>(R.id.BtnReturn)
        val btnSave = findViewById<Button>(R.id.saveButton)
        val btnHelp = findViewById<Button>(R.id.helpButton)

        val ediTxtlocation = findViewById<EditText>(R.id.Location)
        val editTxtPassword1 = findViewById<EditText>(R.id.EdTxtPassword)
        val editTxtPassword2 = findViewById<EditText>(R.id.EdTxtConfPasword)
        val ediTxtName = findViewById<EditText>(R.id.EdTxtName)
        val ediTxtMail = findViewById<EditText>(R.id.EdTxtEmail)

        ediTxtlocation.setText(MainActivity.UserSession.location.toString())
        ediTxtName.setText(MainActivity.UserSession.username.toString())
        ediTxtMail.setText(MainActivity.UserSession.email.toString())

        btnReturn.setOnClickListener{
            finish()
        }

        btnSave.setOnClickListener{
            val userUpdate= UserUpdated(
                ID = MainActivity.UserSession.id!!,
                Nombre = MainActivity.UserSession.username!!,
                Correo = MainActivity.UserSession.email!!,
                Contrasena = MainActivity.UserSession.password!!,
                Ubicacion = MainActivity.UserSession.location!!
            )

            if (ediTxtlocation.text.toString() != ""){
                userUpdate.Ubicacion = ediTxtlocation.text.toString()
                if (ediTxtName.text.toString() != ""){
                    userUpdate.Nombre = ediTxtName.text.toString()
                    if (ediTxtMail.text.toString() != ""){
                        userUpdate.Correo = ediTxtMail.text.toString()
                        if (editTxtPassword1.text.toString() == editTxtPassword2.text.toString() && editTxtPassword1.text.toString() != ""){
                            userUpdate.Contrasena = editTxtPassword1.text.toString()

                            RetrofitClient.instance.updateUser(usuarioMobilId = MainActivity.UserSession.id!!, usuarioRecibido = userUpdate).enqueue(object :
                                Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    if (response.isSuccessful) {
                                        println("Usuario actualizado correctamente: ${response.body()}")

                                         MainActivity.UserSession.username = userUpdate.Nombre
                                         MainActivity.UserSession.email = userUpdate.Correo
                                         MainActivity.UserSession.password = userUpdate.Contrasena
                                         MainActivity.UserSession.location = userUpdate.Ubicacion

                                    } else {
                                        println("Error al actualizar usuario: ${response.message()}")
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    println("Error en la conexión: ${t.message}")
                                }
                            })

                        } else {
                            Toast.makeText(
                                this,
                                "Escribe una contraseña",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Escribe un correo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Escribe un nombre",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Escribe la ubicacion",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val intent = Intent(this@SettingsActivity, NavigationBar::class.java)
            startActivity(intent)

        }

        btnHelp.setOnClickListener {
            lifecycleScope.launch {
                CreateTicketDialog().show(supportFragmentManager, "CreateTicketDialog")
            }
        }
    }
}
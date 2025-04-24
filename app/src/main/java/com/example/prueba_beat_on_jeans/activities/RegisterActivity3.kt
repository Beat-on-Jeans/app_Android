package com.example.prueba_beat_on_jeans.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba_beat_on_jeans.R
import org.bouncycastle.crypto.engines.BlowfishEngine
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.params.KeyParameter

class RegisterActivity3 : AppCompatActivity() {

    private val secretKey = "999a999ale469993"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register3)

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
                MainActivity.UserSession.password = encryptPassword(editTextPassword1.text.toString())
                val intent = Intent(this, RegisterActivity4::class.java)
                startActivity(intent)
            }
        }

        imagebuttonBack.setOnClickListener{
            val intent = Intent(this, RegisterActivity2::class.java)
            startActivity(intent)
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
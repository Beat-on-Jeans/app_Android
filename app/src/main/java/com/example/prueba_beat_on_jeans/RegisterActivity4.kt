package com.example.prueba_beat_on_jeans

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.InputStreamReader

class RegisterActivity4 : AppCompatActivity() {
    private lateinit var postalCodesMap: HashMap<String, String>
    private lateinit var editTextLocation: EditText
    private lateinit var cityText: TextView
    private lateinit var userTemp: UserTemp

    // Data class is not necessary for this case, we will handle the map directly
    // data class PostalCodeData(val code: String, val municipality: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register4)

        // Receive the userTemp object from the previous activity
        userTemp = intent.getParcelableExtra("USER_TEMP") ?: UserTemp()

        // Initialize UI components
        val editTextLocation: EditText = findViewById(R.id.location_edit_text)
        val locationText: TextView = findViewById(R.id.location_text)
        cityText = findViewById(R.id.city_text)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imagebuttonBack: ImageButton = findViewById(R.id.back_imageButton)

        // Load postal codes map from JSON file
        postalCodesMap = loadPostalCodes()

        // Set text depending on user role
        when (userTemp.rolId) {
            1 -> {
                locationText.text = "Añade tu código postal"

                editTextLocation.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val postalCode = s.toString()
                        if (postalCodesMap.containsKey(postalCode)) {
                            val city = postalCodesMap[postalCode]
                            cityText.text = city
                        } else {
                            cityText.text = "..."
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                // Continue button click event
                buttonContinue.setOnClickListener {
                    if (cityText.text == "...") {
                        Toast.makeText(this, "Escribe el código postal", Toast.LENGTH_SHORT).show()
                    } else {
                        userTemp.ubicacion = cityText.text.toString()
                        val intent = Intent(this, RegisterActivity5::class.java)
                        intent.putExtra("USER_TEMP", userTemp)
                        startActivity(intent)
                    }
                }

            }
            2 -> {
                cityText.text = ""
            }
        }
        
        // Back button click event
        imagebuttonBack.setOnClickListener {
            val intent = Intent(this, RegisterActivity3::class.java)
            intent.putExtra("USER_TEMP", userTemp)
            startActivity(intent)
        }
    }

    // Function to load postal codes from the JSON file in assets
    private fun loadPostalCodes(): HashMap<String, String> {
        val codigosPostalesMap = hashMapOf<String, String>()

        try {
            // Open the postal_codes.json file from the assets folder
            val inputStream = assets.open("postal_codes.json")
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()

            // Parse the JSON string as a JSONArray
            val jsonArray = JSONArray(jsonString)

            // Iterate over the array and add postal codes to the map
            for (i in 0 until jsonArray.length()) {
                val row = jsonArray.getJSONArray(i)
                val postalCode = row.getString(9)
                val municipality = row.getString(11)
                codigosPostalesMap[postalCode] = municipality
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return codigosPostalesMap
    }
}

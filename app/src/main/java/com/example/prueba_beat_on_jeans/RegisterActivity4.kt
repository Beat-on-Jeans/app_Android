package com.example.prueba_beat_on_jeans

import android.content.Context
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
    private lateinit var addresses: List<Address> // Added the addresses list
    private lateinit var editTextLocation: EditText
    private lateinit var cityText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register4)

        val editTextLocation: EditText = findViewById(R.id.location_edit_text)
        val locationText: TextView = findViewById(R.id.location_text)
        cityText = findViewById(R.id.city_text)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imagebuttonBack: ImageButton = findViewById(R.id.back_imageButton)

        // Load postal codes and addresses from JSON
        postalCodesMap = loadPostalCodes()
        addresses = loadAddressesFromJson(this)  // Load the addresses list

        when (MainActivity.UserSession.rolId) {
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

                buttonContinue.setOnClickListener {
                    if (cityText.text == "...") {
                        Toast.makeText(this, "Escribe el código postal", Toast.LENGTH_SHORT).show()
                    } else {
                        MainActivity.UserSession.location = cityText.text.toString()
                        val intent = Intent(this, RegisterActivity5::class.java)
                        startActivity(intent)
                    }
                }
            }
            2 -> {
                cityText.text = ""

                editTextLocation.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val input = s.toString().trim()

                        if (input.isNotEmpty()) {
                            val parts = input.split(",", limit = 2).map { it.trim() }

                            if (parts.size == 2) {
                                val street = parts[0]
                                val number = parts[1]

                                val foundAddress = findAddress(street, number)

                                cityText.text = foundAddress?.let {
                                    "${it.shortName}, $number, Barcelona"
                                } ?: "Calle no encontrada"
                            } else {
                                cityText.text = "'Calle, número'"
                            }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                buttonContinue.setOnClickListener {
                    if (cityText.text.toString().isEmpty() || cityText.text.toString() == "Calle no encontrada" || cityText.text.toString() == "'Calle, número'") {
                        Toast.makeText(this, "Escribe la dirección del local", Toast.LENGTH_SHORT).show()
                    } else {
                        MainActivity.UserSession.location = cityText.text.toString()
                        val intent = Intent(this, RegisterActivity5::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        imagebuttonBack.setOnClickListener {
            val intent = Intent(this, RegisterActivity3::class.java)
            startActivity(intent)
        }
    }

    data class Address(
        // Campos almacenados pero no usados en la validación
        val codiVia: String,          // "codi_via" del JSON
        val codiCarrerIne: String,    // "codi_carrer_ine" del JSON
        val tipusVia: String,         // "tipus_via" del JSON

        // Campos usados en la lógica de validación
        val shortName: String,        // "nom_curt" del JSON (ej: "GV Corts Catalanes")
        val officialName: String,     // "nom_oficial" del JSON (ej: "Gran Via de les Corts Catalanes")
        val minNumber: Int?,          // "nre_min" convertido a Int (ej: 111)
        val maxNumber: Int?           // "nre_max" convertido a Int (ej: 1198)
    )



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
                val postalCode = row.getString(9)  // Ensure these indices match your actual JSON structure
                val municipality = row.getString(11) // Ensure these indices match your actual JSON structure
                codigosPostalesMap[postalCode] = municipality
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return codigosPostalesMap
    }

    // Function to load addresses from the streets.json file
    private fun loadAddressesFromJson(context: Context): List<Address> {
        val addressesList = mutableListOf<Address>()

        try {
            val inputStream = context.assets.open("streets.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                // Parsear todos los campos del JSON
                addressesList.add(
                    Address(
                        codiVia = obj.optString("codi_via", ""),
                        codiCarrerIne = obj.optString("codi_carrer_ine", ""),
                        tipusVia = obj.optString("tipus_via", ""),
                        shortName = obj.optString("nom_curt", ""),
                        officialName = obj.optString("nom_oficial", ""),
                        minNumber = obj.optString("nre_min", "").toIntOrNull(),
                        maxNumber = obj.optString("nre_max", "").toIntOrNull()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressesList
    }

    private fun findAddress(street: String, number: String): Address? {
        return addresses.firstOrNull { address ->
            (address.shortName.equals(street, ignoreCase = true) ||
                    address.officialName.equals(street, ignoreCase = true))
                    && isValidNumber(number, address.minNumber, address.maxNumber)
        }
    }

    private fun isValidNumber(numberStr: String, min: Int?, max: Int?): Boolean {
        return try {
            val number = numberStr.toInt()
            (min == null || number >= min) && (max == null || number <= max)
        } catch (e: NumberFormatException) {
            false
        }
    }
}

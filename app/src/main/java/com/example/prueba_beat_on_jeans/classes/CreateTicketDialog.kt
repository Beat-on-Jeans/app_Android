package com.example.prueba_beat_on_jeans.classes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.classes.EditDescriptionDialog.Companion.CURRENT_DESCRIPTION
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTicketDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_ticket, container, false)
        val spinner = view.findViewById<Spinner>(R.id.incidence_name)
        val sendButton = view.findViewById<Button>(R.id.buttonSend)
        var selectedIncidenceId = 0

        val items = listOf(
            "Error al abrir la aplicación",
            "La aplicación no encuentra más usuarios",
            "Problemas de red",
            "Problemas con otro usuario",
            "Falla en la gestión de un evento",
            "Cancelación de evento sin previo aviso",
            "Problema con la ubicación del local",
            "Músico no se presentó al evento",
            "Local no permitió la presentación",
            "Solicitud de soporte técnico"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedIncidenceId = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nada seleccionado
            }
        }


        sendButton.setOnClickListener {
            val userId = MainActivity.UserSession.id

            RetrofitClient.instance.crearIncidencia(userId!!, selectedIncidenceId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Log.d("API", "Incidencia enviada correctamente")
                            Toast.makeText(context, "Incidencia enviada", Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            Log.e("API", "Error al enviar: ${response.code()}")
                            Toast.makeText(context, "Error al enviar incidencia", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("API", "Fallo de red", t)
                        Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show()
                    }
                })
        }


        return view
    }

}

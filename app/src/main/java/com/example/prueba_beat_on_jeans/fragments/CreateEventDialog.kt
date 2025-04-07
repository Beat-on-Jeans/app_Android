package com.example.prueba_beat_on_jeans.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.api.Event
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.User
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class CreateEventDialog : DialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val eventFinalizer = arguments?.getInt("USERID")?: -1
        if (eventFinalizer != -1){

            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_create_new_event, null)

            val btnConfirm = dialogView.findViewById<Button>(R.id.btnCreate)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
            val btnAddDate = dialogView.findViewById<Button>(R.id.btnAddDate)
            val tvSelectedDate = dialogView.findViewById<TextView>(R.id.txt_selected_date)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .create()

            var eventDate = "0000-00-00"

            btnAddDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    tvSelectedDate.text = "Fecha: $selectedDate"
                    eventDate = selectedDate
                }

                if (!tvSelectedDate.text.isNullOrBlank() && !tvSelectedDate.text.isNullOrEmpty()) {
                    btnConfirm.isEnabled = true
                }
            }

            btnConfirm.setOnClickListener {
                if(btnConfirm.isEnabled){
                    val newEvent = Event(eventDate,MainActivity.UserSession.id!!,
                        eventFinalizer,2)
                    createNewEvent(newEvent)
                    btnConfirm.isEnabled = false
                    dialog.dismiss()
                }

            }

            btnCancel.setOnClickListener {
                btnConfirm.isEnabled = false
                dialog.dismiss()
            }
            return dialog
        }else{
            if (id == -1) {
                return AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("No se pudo obtener el ID del evento.")
                    .setPositiveButton("OK") { _, _ -> }
                    .create()
            }

        }
        return dialog!!
    }

    private fun createNewEvent(newEvent: Event) {
        lifecycleScope.launch {
            RetrofitClient.instance.createNewEvent(newEvent)
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            onDateSelected(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    companion object {
        fun newInstance(id: Int): CreateEventDialog {
            val args = Bundle()
            args.putInt("USERID", id)

            val fragment = CreateEventDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
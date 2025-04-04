package com.example.prueba_beat_on_jeans.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.api.Event
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.User
import kotlinx.coroutines.launch

class AcceptEventDialog: DialogFragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.layout_accept_new_event, null)

        val btnConfirm = dialogView.findViewById<Button>(R.id.btnCreate)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val eventCreator = arguments?.getSerializable("USER") as User
        val event = arguments?.getSerializable("EVENT") as Event

        btnConfirm.setOnClickListener {
            acceptEvent(event)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dismissEvent(event)
            dialog.dismiss()
        }

        return dialog
    }

    private fun acceptEvent(event :Event) {
        lifecycleScope.launch {
            event.estado = 3
            RetrofitClient.instance.createEvent(event)
        }
    }

    private fun dismissEvent(event :Event) {
        lifecycleScope.launch {
            RetrofitClient.instance.deleteEvent(event)
        }
    }


    companion object {
        fun newInstance(user: User, event: Event): AcceptEventDialog {
            val args = Bundle()
            args.putSerializable("USER", user)
            args.putSerializable("EVENT", event)

            val fragment = AcceptEventDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
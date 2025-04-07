package com.example.prueba_beat_on_jeans.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.api.Event
import com.example.prueba_beat_on_jeans.api.Rating
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.User
import kotlinx.coroutines.launch

class NewRattingEventDialog: DialogFragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.layout_ratting_event, null)

        val btnConfirm = dialogView.findViewById<Button>(R.id.btnNewRatting)
        val rbEventRatting = dialogView.findViewById<RatingBar>(R.id.RBEventRatting)
        val txtRatting = dialogView.findViewById<TextView>(R.id.txtRatting)


        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val rating = arguments?.getSerializable("RATING") as Rating

        rbEventRatting.setOnRatingBarChangeListener { rBar, _, _ ->
            when(rBar.rating.toInt()){
                1 -> txtRatting.text = "Muy Mala"
                2 -> txtRatting.text = "Mala"
                3 -> txtRatting.text = "Media"
                4 -> txtRatting.text = "Buena"
                5 -> txtRatting.text = "Muy Buena"
                else -> txtRatting.text = ""
            }
            rating.rating = rBar.rating.toInt()

            btnConfirm.isEnabled = true
        }

        btnConfirm.setOnClickListener {

            acceptNewValoration(rating)
            dialog.dismiss()
        }

        return dialog
    }

    private fun acceptNewValoration(rating: Rating) {
        lifecycleScope.launch {
            RetrofitClient.instance.setRatting(rating)
        }
    }

    companion object {
        fun newInstance(rating: Rating): NewRattingEventDialog {
            val args = Bundle()
            args.putSerializable("RATING", rating)

            val fragment = NewRattingEventDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
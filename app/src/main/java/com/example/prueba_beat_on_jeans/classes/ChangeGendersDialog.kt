package com.example.prueba_beat_on_jeans.classes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ChangeGendersDialog(
    private val generosMusicales: List<String>,
    private val selectedIndices: List<Int>,
    private val listener: OnGendersSelectedListener
) : DialogFragment() {

    interface OnGendersSelectedListener {
        fun onGendersSelected(selectedIndices: List<Int>)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Crear array de selección inicial
        val selectedItems = BooleanArray(generosMusicales.size).apply {
            selectedIndices.forEach { index ->
                if (index in 0 until generosMusicales.size) {
                    this[index] = true
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Selecciona tus géneros musicales")
            .setMultiChoiceItems(
                generosMusicales.toTypedArray(),
                selectedItems
            ) { _, which, isChecked ->
                selectedItems[which] = isChecked
            }
            .setPositiveButton("Guardar") { _, _ ->
                val selected = selectedItems.indices
                    .filter { selectedItems[it] }
                    .toList()
                listener.onGendersSelected(selected)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}
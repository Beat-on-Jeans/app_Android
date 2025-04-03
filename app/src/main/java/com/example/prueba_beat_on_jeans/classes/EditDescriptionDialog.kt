package com.example.prueba_beat_on_jeans.classes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.prueba_beat_on_jeans.R

class EditDescriptionDialog : DialogFragment() {
    private var listener: OnDescriptionChangedListener? = null

    interface OnDescriptionChangedListener {
        fun onDescriptionChanged(newDescription: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnDescriptionChangedListener) {
            listener = parentFragment as OnDescriptionChangedListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_edit_description, container, false)
        val editText = view.findViewById<EditText>(R.id.editTextDescription)
        val saveButton = view.findViewById<Button>(R.id.buttonSaveDescription)

        arguments?.getString(CURRENT_DESCRIPTION)?.let {
            editText.setText(it)
        }

        saveButton.setOnClickListener {
            val newDescription = editText.text.toString().trim()
            if (newDescription.isNotEmpty()) {
                listener?.onDescriptionChanged(newDescription)
                dismiss()
            } else {
                editText.error = "La descripción no puede estar vacía"
            }
        }

        return view
    }

    companion object {
        const val CURRENT_DESCRIPTION = "current_description"

        fun newInstance(currentDescription: String, listener: OnDescriptionChangedListener): EditDescriptionDialog {
            return EditDescriptionDialog().apply {
                this.listener = listener
                arguments = Bundle().apply {
                    putString(CURRENT_DESCRIPTION, currentDescription)
                }
            }
        }
    }
}
package com.example.trakkamap.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.trakkamap.R
import java.io.File

class NameDialog(private val previousView : View) : DialogFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            // Use the Builder class for convenient dialog construction.
            val dialogView = inflater.inflate(R.layout.name_dialog, null) // Inflate the dialog layout

            val builder = AlertDialog.Builder(it)
            builder.setView(dialogView)
                .setPositiveButton("Confirm") { _, _ ->
                    val editText = dialogView.findViewById<EditText>(R.id.username).text.toString() // Replace 'editText' with the actual ID of your EditText
                    setGreeting(editText)
                    previousView.findViewById<TextView>(R.id.name_text).text = ("Hi, $editText")
                }
                .setNegativeButton("Cancel") { _, _ ->

                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setGreeting(name : String) {
        val nameFile = File(requireContext().filesDir,"name.txt")
        nameFile.writeText(name)
    }
}
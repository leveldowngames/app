package com.example.trakkamap.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.io.File

class ConfirmationDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setMessage("This action will delete your entire location history. It cannot be undone. Are you sure?")
                .setPositiveButton("Delete") { _, _ ->
                    val fileA = File(requireContext().filesDir, "recordsA.txt")
                    val fileB = File(requireContext().filesDir, "recordsB.txt")
                    val fileC = File(requireContext().filesDir, "recordsC.txt")
                    val fileD = File(requireContext().filesDir, "recordsD.txt")
                    val fileE = File(requireContext().filesDir, "recordsE.txt")

                    fileA.writeText("")
                    fileB.writeText("")
                    fileC.writeText("")
                    fileD.writeText("")
                    fileE.writeText("")
                }
                .setNegativeButton("Cancel") { _, _ ->

                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
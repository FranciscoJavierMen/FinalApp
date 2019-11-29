package com.example.finalapp.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.finalapp.R

class RateDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.rate_dialog)
            .setPositiveButton(getString(R.string.dialog_ok)){ dialog, wich ->
                Toast.makeText(context, "Pressed ok button", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)){ dialog, wich ->
                Toast.makeText(context, "Pressed cancel button", Toast.LENGTH_SHORT).show()
            }
            .create()
    }
}
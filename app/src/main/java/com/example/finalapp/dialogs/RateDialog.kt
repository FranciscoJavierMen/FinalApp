package com.example.finalapp.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.finalapp.R
import com.example.finalapp.models.Rate
import com.example.finalapp.models.RateEvent
import com.example.finalapp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.rate_dialog.view.*
import java.util.*

class RateDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(context).inflate(R.layout.rate_dialog, null, false)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                val textRate = view.edtRateNote.editText?.text.toString()
                if(textRate.isNotEmpty()){
                    val imgURL = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString() ?: run{ "" }
                    val rate = Rate(textRate, view.ratingBar.rating, Date(), imgURL)
                    RxBus.publish(RateEvent(rate))
                } else {
                    Toast.makeText(context, "The rate description cannot be empty", Toast.LENGTH_SHORT).show()
                }

            }
            .setNegativeButton(getString(R.string.dialog_cancel)){ _, _ ->
                Toast.makeText(context, "Action cancelled", Toast.LENGTH_SHORT).show()
            }
            .create()
    }
}
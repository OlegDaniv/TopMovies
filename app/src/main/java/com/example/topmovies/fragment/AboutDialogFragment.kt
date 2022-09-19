package com.example.topmovies.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.topmovies.R

class AboutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.about_dialog_fragment_title))
            .setMessage(getString(R.string.about_dialog_fragment_message))
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()
    }
}

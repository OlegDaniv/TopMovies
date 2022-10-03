package com.example.topmovies.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.topmovies.databinding.NoInternetDialogBinding

class NetworkDialogFragment : DialogFragment() {
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = NoInternetDialogBinding.inflate(layoutInflater)
        binding.buttonRetry.setOnClickListener {
            dialog?.dismiss()
        }
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setView(binding.root)
        dialog.create()
        return dialog.show()
    }
}
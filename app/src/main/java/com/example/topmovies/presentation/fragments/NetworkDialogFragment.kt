package com.example.topmovies.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.topmovies.databinding.NoInternetDialogBinding

class NetworkDialogFragment : DialogFragment() {

    private var _binding: NoInternetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = NoInternetDialogBinding.inflate(layoutInflater)
        binding.buttonRetry.setOnClickListener {
            dialog?.dismiss()
        }
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(binding.root)
        dialog.create()
        return dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

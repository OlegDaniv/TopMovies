package com.example.topmovies.ui.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.topmovies.exeption.Failure
import com.example.topmovies.exeption.Failure.NetworkConnection
import com.example.topmovies.exeption.Failure.ServerError

abstract class BaseFragment : Fragment() {

    fun handlerFailure(failure: Failure) {
        when (failure) {
            is ServerError -> Toast.makeText(context, "ServerError", Toast.LENGTH_SHORT).show()
            is NetworkConnection -> NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
}

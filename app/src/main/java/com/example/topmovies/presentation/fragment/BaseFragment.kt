package com.example.topmovies.presentation.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Error.NetworkConnection
import com.example.topmovies.domain.utils.Error.ServerError

abstract class BaseFragment : Fragment() {

    fun handleFailure(failure: Error) {
        when (failure) {
            is ServerError -> Toast.makeText(context, "ServerError", Toast.LENGTH_SHORT).show()
            is NetworkConnection -> NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
}

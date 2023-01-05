package com.example.topmovies.presentation.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Failure.NetworkConnection
import com.example.topmovies.domain.utils.Failure.ServerError

abstract class BaseFragment : Fragment() {

    fun handlerFailure(failure: Failure) {
        when (failure) {
            is ServerError -> Toast.makeText(context, "ServerError", Toast.LENGTH_SHORT).show()
            is NetworkConnection -> NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
}

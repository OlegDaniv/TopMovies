package com.example.topmovies.presentation.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.domain.utils.Error
import com.example.domain.utils.Error.NetworkConnectionError
import com.example.domain.utils.Error.ServerError
import com.example.topmovies.R

abstract class BaseFragment : Fragment() {

    fun showErrorMessage(errorMessage: Error) {
        when (errorMessage) {
            is ServerError -> Toast.makeText(
                context, getString(R.string.server_error), Toast.LENGTH_SHORT
            ).show()
            is NetworkConnectionError -> NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
}

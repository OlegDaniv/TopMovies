package com.example.topmovies.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.domain.utils.Error
import com.example.domain.utils.Error.NetworkConnectionError
import com.example.domain.utils.Error.ServerError
import com.example.topmovies.R

abstract class BaseFragment : Fragment() {

    fun showErrorMessage(errorMassage: Error) {
        when (errorMassage) {
            is ServerError -> Toast.makeText(
                context, getString(R.string.server_error), Toast.LENGTH_SHORT
            ).show()
            is NetworkConnectionError -> NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
}

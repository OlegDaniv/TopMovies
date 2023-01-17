package com.example.topmovies.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.topmovies.R
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Error.NetworkConnectionError
import com.example.topmovies.utils.Error.ServerError

abstract class BaseFragment : Fragment() {

    fun showErrorMessage(errorMassage: Error) {
        when (errorMassage) {
            is ServerError -> Toast.makeText(
                context, getString(R.string.ServerError), Toast.LENGTH_SHORT
            ).show()
            is NetworkConnectionError -> NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
}

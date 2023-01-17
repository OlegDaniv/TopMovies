package com.example.topmovies.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.topmovies.utils.Error

abstract class BaseFragment : Fragment() {
    
    fun showErrorMassage(errorMassage: Error) {
        if (isNetworkAvailable()) {
            Toast.makeText(context,"errorMassage.", Toast.LENGTH_SHORT).show()
        } else {
            NetworkDialogFragment().show(parentFragmentManager, null)
        }
    }
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}

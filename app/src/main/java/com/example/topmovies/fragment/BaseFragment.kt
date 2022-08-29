package com.example.topmovies.fragment

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    private val TAG = BaseFragment::class.simpleName
    var toolBarBridge: ToolBarBridge? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            toolBarBridge = requireActivity() as ToolBarBridge
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
        }
    }
}
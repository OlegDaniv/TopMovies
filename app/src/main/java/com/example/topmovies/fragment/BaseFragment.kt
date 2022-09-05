package com.example.topmovies.fragment

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), ToolbarBehaviour {
    private val toolBarBridge: ToolbarBridge by lazy { requireActivity() as ToolbarBridge }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolBarBridge
    }

    override fun showUpButton() = toolBarBridge.showUpButton()

    override fun hideUpButton() = toolBarBridge.hideUpButton()
}

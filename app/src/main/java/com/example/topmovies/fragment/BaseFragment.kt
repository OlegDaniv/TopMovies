package com.example.topmovies.fragment

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

abstract class BaseFragment : Fragment(), ToolbarBehaviour {
    
    private val toolBarBridge: ToolbarBridge by lazy { requireActivity() as ToolbarBridge }
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolBarBridge
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
    
    override fun showBackButton() = toolBarBridge.showBackButton()
    
    override fun hideBackButton() = toolBarBridge.hideBackButton()

    fun getApiKey(): String {
        val apiKey = sharedPreferences.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY)
        if (apiKey != null) {
            if (apiKey == "") return DEF_API_KEY
            return apiKey
        } else throw NullPointerException("Preference is not found!!")
    }

}

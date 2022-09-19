package com.example.topmovies.fragment

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

abstract class BaseFragment : Fragment() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
    
    fun getApiKey(): String {
        sharedPreferences.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY).also {
            if (it != null) {
                if (it == "") return DEF_API_KEY
                return it
            } else throw NullPointerException("Preference is not found!!")
        }
    }
}

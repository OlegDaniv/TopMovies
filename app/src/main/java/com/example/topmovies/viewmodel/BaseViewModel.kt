package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

abstract class BaseViewModel(private val sharedPref: SharedPreferences) : ViewModel() {
    
    fun getApiKey() =
        sharedPref.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY)?.takeIf { it.isNotBlank() }
            ?: DEF_API_KEY
}

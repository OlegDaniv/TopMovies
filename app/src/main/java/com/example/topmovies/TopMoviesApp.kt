package com.example.topmovies

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.PreferenceManager
import com.example.topmovies.di.appModule
import com.example.topmovies.unit.SETTING_PREF_THEME
import com.example.topmovies.unit.THEME_MODE_AUTO
import com.example.topmovies.unit.THEME_MODE_DARK
import com.example.topmovies.unit.THEME_MODE_LIGHT
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TopMoviesApp : Application() {
    
    private val prefManager: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }
    
    override fun onCreate() {
        super.onCreate()
        checkNightMode()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TopMoviesApp)
            modules(appModule)
        }
    }
    
    private fun checkNightMode() {
        when (prefManager.getString(SETTING_PREF_THEME, "")) {
            THEME_MODE_AUTO -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            THEME_MODE_DARK -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            THEME_MODE_LIGHT -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

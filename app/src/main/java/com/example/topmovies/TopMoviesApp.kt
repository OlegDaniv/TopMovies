package com.example.topmovies

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.topmovies.di.appModule
import com.example.topmovies.unit.SETTING_PREF_THEME
import com.example.topmovies.unit.checkNightMode
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
        checkNightMode(prefManager.getString(SETTING_PREF_THEME, ""))
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TopMoviesApp)
            modules(appModule)
        }
    }
}

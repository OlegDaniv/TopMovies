package com.example.topmovies

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.topmovies.di.appModule
import com.example.topmovies.unit.SETTING_PREF_THEME
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TopMoviesApp : Application() {
    
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
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SETTING_PREF_THEME, false)
        ) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

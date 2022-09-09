package com.example.topmovies

import android.app.Application
import android.util.Log
import com.example.topmovies.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule)
        }
        someMethod()
    }

    fun someMethod(){
        Log.e("Logs", "App.someMethod() -> ${BuildConfig.VERSION_CODE}")
    }
}
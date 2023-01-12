package com.example.topmovies.presentation.utils

import androidx.appcompat.app.AppCompatDelegate

class ThemeHandler {

    companion object {
        fun checkNightMode(mode: String?) {
            AppCompatDelegate.setDefaultNightMode(
                when (mode) {
                    THEME_MODE_AUTO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    THEME_MODE_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    THEME_MODE_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }

        private const val THEME_MODE_AUTO = "Auto"
        private const val THEME_MODE_LIGHT = "Light"
        private const val THEME_MODE_DARK = "Dark"
    }
}

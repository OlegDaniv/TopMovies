package com.example.topmovies.unit

import androidx.appcompat.app.AppCompatDelegate.*

fun checkNightMode(mode: String?) {
    setDefaultNightMode(
        when (mode) {
            THEME_MODE_AUTO -> MODE_NIGHT_FOLLOW_SYSTEM
            THEME_MODE_DARK -> MODE_NIGHT_YES
            THEME_MODE_LIGHT -> MODE_NIGHT_NO
            else -> MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}

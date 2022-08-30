package com.example.topmovies.preferences

import android.content.Context

class SharedPref(private val context: Context, name: String) {


    private val sharesPreference by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun removeFavoriteMovie(key: String) =
        sharesPreference.edit().remove(key).apply()


    fun saveFavoriteMovie(key: String, favorite: Boolean) {
        sharesPreference.edit().putBoolean(key, favorite).apply()
    }

    fun movieIsFavorite(key: String) = sharesPreference.getBoolean(key, true)
}

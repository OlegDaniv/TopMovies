package com.example.topmovies.data.utils

import android.content.SharedPreferences

/** When using api.getMovies() and api.getMovieDetails() without any parameters, it connects to a mock server.
 *  If you wish to utilize the imdb server, put the return value in api.getMovies(getApiKey()) and
 *  api.getMovieDetails(id),getApiKey()).
 *  id - movie id in the imdb server**/
class GetPreference(
    private val sharedPreferences: SharedPreferences
) {

    fun getApiKey() =
        sharedPreferences.getString(
            SETTING_PREF_USER_API_KEY,
            DEF_API_KEY
        )
            ?.takeIf { it.isNotBlank() }
            ?: DEF_API_KEY

    companion object {
        private const val DEF_API_KEY = "k_efexam0h"
        private const val SETTING_PREF_USER_API_KEY = "api_key_preference"
    }
}

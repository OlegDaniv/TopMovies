package com.example.topmovies.utils

import android.content.SharedPreferences
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

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
}

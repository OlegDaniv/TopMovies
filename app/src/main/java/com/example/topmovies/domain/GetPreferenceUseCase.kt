package com.example.topmovies.domain

import android.content.SharedPreferences
import com.example.topmovies.datalayer.network.MoviesRequest

/** when using api.getMovies() and api.getMovieDetails() without any parameters, it connects to a mock server.
 *  If you wish to utilize the imdb server, put the return value in api.getMovies(getApiKey()) and
 *  api.getMovieDetails(id),getApiKey()).
 *  id - movie id in the imdb server**/
class GetPreferenceUseCase(
    private val sharedPreferences: SharedPreferences
) {

    fun getApiKey() =
        sharedPreferences.getString(
            MoviesRequest.SETTING_PREF_USER_API_KEY,
            MoviesRequest.DEF_API_KEY
        )
            ?.takeIf { it.isNotBlank() }
            ?: MoviesRequest.DEF_API_KEY
}

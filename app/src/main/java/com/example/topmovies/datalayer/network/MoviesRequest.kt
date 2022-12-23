package com.example.topmovies.datalayer.network

import com.example.topmovies.datalayer.repository.MoviesRepository.Movie
import com.example.topmovies.domain.GetPreferenceUseCase
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.utils.ResultOf
import com.example.topmovies.utils.request

class MoviesRequest(
    private val api: MoviesApi,
    private val getPreference: GetPreferenceUseCase,
    private val networkHandler: NetworkHandler
) {
    companion object {
        const val SETTING_PREF_USER_API_KEY = "api_key_preference"
        const val DEF_API_KEY = "k_efexam0h"
    }

    fun getNewMovies(): ResultOf<Failure, List<Movie>> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> request(
                api.getMovies(),
                { it.items.map { movieApi -> movieApi.toMovie() } },
                MoviesApi.MovieObject.empty
            )
            false -> ResultOf.Failed(Failure.NetworkConnection)
        }
    }
}

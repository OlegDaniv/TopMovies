package com.example.topmovies.data.network

import com.example.topmovies.data.models.response.MovieObjectResponse
import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.domain.exeption.Failure
import com.example.topmovies.domain.usecase.GetPreferenceUseCase
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.presentation.models.Movie

class MoviesRequest(
    private val api: MoviesApi,
    private val getPreference: GetPreferenceUseCase,
    private val networkHandler: NetworkHandler
) : BaseRequest() {
    companion object {
        const val SETTING_PREF_USER_API_KEY = "api_key_preference"
        const val DEF_API_KEY = "k_efexam0h"
    }

    fun getNewMovies(): ResultOf<Failure, List<Movie>> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> request(
                api.getMovies(),
                { it.items.map { movieApi -> movieApi.toMovie() } },
                MovieObjectResponse.empty
            )
            false -> ResultOf.Failed(Failure.NetworkConnection)
        }
    }
}

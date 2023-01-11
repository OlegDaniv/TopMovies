package com.example.topmovies.data.network

import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.domain.usecase.GetPreferenceUseCase
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Error
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

    fun getNewMovies(): Result<Failure, List<Movie>> {
        return if (networkHandler.isNetworkAvailable()) {
            request(
                api.getMovies(),
            ) {
                it.items.map { movieApi -> movieApi.toMovie() }
            }
        } else {
            Error(Failure.NetworkConnection)
        }
    }
}

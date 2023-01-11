package com.example.topmovies.data.network

import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.domain.usecase.GetPreferenceUseCase
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.presentation.models.MovieDetails

class MovieDetailsRequest(
    private val api: MovieDetailsAPi,
    private val getPreference: GetPreferenceUseCase,
    private val networkHandler: NetworkHandler
) : BaseRequest() {

    fun loadMovieDetails(
        movieId: String,
    ): Result<Failure, MovieDetails> {
        return if (networkHandler.isNetworkAvailable()) {
            request(
                /** use parameter in api.getMovieDetails() to use IMDB api   **/
                api.getMovieDetails(),
            ) { it.toMovieDetails() }
        } else {
            Result.Error(Failure.NetworkConnection)
        }
    }
}

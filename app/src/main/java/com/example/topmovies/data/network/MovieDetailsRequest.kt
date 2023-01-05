package com.example.topmovies.data.network

import com.example.topmovies.data.models.response.MovieDetailsResponse
import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Failure.NetworkConnection
import com.example.topmovies.domain.usecase.GetPreferenceUseCase
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.domain.utils.ResultOf.Failed
import com.example.topmovies.presentation.models.MovieDetails

class MovieDetailsRequest(
    private val api: MovieDetailsAPi,
    private val getPreference: GetPreferenceUseCase,
    private val networkHandler: NetworkHandler
) : BaseRequest() {

    fun loadMovieDetails(
        movieId: String,
    ): ResultOf<Failure, MovieDetails> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> request(
                /** use parameter in api.getMovieDetails() to use IMDB api   **/
                api.getMovieDetails(),
                { it.toMovieDetails() },
                MovieDetailsResponse.empty
            )
            false -> Failed(NetworkConnection)
        }
    }
}

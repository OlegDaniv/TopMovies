package com.example.topmovies.datalayer.network

import com.example.topmovies.datalayer.repository.MovieDetailsRepository.MovieDetails
import com.example.topmovies.domain.GetPreferenceUseCase
import com.example.topmovies.exeption.Failure
import com.example.topmovies.exeption.Failure.NetworkConnection
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.utils.ResultOf
import com.example.topmovies.utils.ResultOf.Failed
import com.example.topmovies.utils.request

class MovieDetailsRequest(
    private val api: MovieDetailsAPi,
    private val getPreference: GetPreferenceUseCase,
    private val networkHandler: NetworkHandler
) {

    fun loadMovieDetails(
        movieId: String,
    ): ResultOf<Failure, MovieDetails> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> request(
                /** use parameter in api.getMovieDetails() to use IMDB api   **/
                api.getMovieDetails(),
                { it.toMovieDetails() },
                MovieDetailsAPi.MovieDetailsBody.empty
            )
            false -> Failed(NetworkConnection)
        }
    }
}

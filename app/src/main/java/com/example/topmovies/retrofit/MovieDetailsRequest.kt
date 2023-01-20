package com.example.topmovies.retrofit

import com.example.domain.models.MovieDetails
import com.example.domain.utils.Error
import com.example.domain.utils.Error.NetworkConnectionError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.topmovies.models.mapper.MovieDetailsResponseMapper
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.utils.safeTransform

class MovieDetailsRequest(
    private val api: MovieDetailsApi,
    private val networkHandler: NetworkHandler
) {
    fun loadNewMovieDetails(): Result<Error, MovieDetails> {
        return if (networkHandler.isNetworkAvailable()) {
            api.getMovieDetails().safeTransform {
                MovieDetailsResponseMapper.toModel(it)
            }
        } else {
            Failure(NetworkConnectionError)
        }
    }
}

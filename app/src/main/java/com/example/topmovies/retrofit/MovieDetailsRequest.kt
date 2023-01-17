package com.example.topmovies.retrofit

import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.mapper.MovieDetailsResponseMapper
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.utils.Result
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
            Result.Failure(Error.NetworkConnectionError)
        }
    }
}

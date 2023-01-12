package com.example.topmovies.data.network

import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.data.utils.unwrap
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.GetPreference
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Failure
import com.example.topmovies.presentation.models.MovieDetails

class MovieDetailsRequest(
    private val api: MovieDetailsApi,
    private val getPreference: GetPreference,
    private val networkHandler: NetworkHandler
) {

    fun loadMovieDetails(
        movieId: String,
    ): Result<Error, MovieDetails> {
        return if (networkHandler.isNetworkAvailable()) {
            api.getMovieDetails().unwrap {
                /** Use parameter in api.getMovieDetails() to use IMDB api   **/
                it.toMovieDetails()
            }
        } else {
            Failure(Error.NetworkConnection)
        }
    }
}

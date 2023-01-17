package com.example.topmovies.retrofit

import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.mapper.MovieResponseMapper
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Error.NetworkConnectionError
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.safeResponse

class MoviesRequest(
    private val api: MoviesApi,
    private val networkHandler: NetworkHandler
) {
    fun loadNewMovies(): Result<Error, List<Movie>> {
        return if (networkHandler.isNetworkAvailable()) {
            api.getMovies().safeResponse { MovieObject ->
                MovieObject.items.map { MovieResponseMapper.toModel(it) }
            }
        } else {
            Result.Failure(NetworkConnectionError)
        }
    }

}

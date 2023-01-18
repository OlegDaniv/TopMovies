package com.example.topmovies.retrofit

import com.example.domain.models.Movie
import com.example.domain.utils.Error
import com.example.domain.utils.Error.NetworkConnectionError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.topmovies.models.mapper.MovieResponseMapper
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.utils.safeTransform

class MoviesRequest(
    private val api: MoviesApi,
    private val networkHandler: NetworkHandler
) {
    fun loadNewMovies(): Result<Error, List<Movie>> {
        return if (networkHandler.isNetworkAvailable()) {
            api.getMovies().safeTransform { movieObject ->
                movieObject.items.map { MovieResponseMapper.toModel(it) }
            }
        } else {
            Failure(NetworkConnectionError)
        }
    }
}

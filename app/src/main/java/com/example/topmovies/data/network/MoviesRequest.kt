package com.example.topmovies.data.network

import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.data.utils.unwrap
import com.example.topmovies.domain.utils.GetPreference
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Failure
import com.example.topmovies.presentation.models.Movie

class MoviesRequest(
    private val api: MoviesApi,
    /** Use this reference to obtain a key. **/
    private val getPreference: GetPreference,
    private val networkHandler: NetworkHandler
) {

    fun getNewMovies(): Result<Error, List<Movie>> {
        return if (networkHandler.isNetworkAvailable()) {
            api.getMovies().unwrap {
                it.items.map { movieApi -> movieApi.toMovie() }
            }
        } else {
            Failure(Error.NetworkConnection)
        }
    }
}

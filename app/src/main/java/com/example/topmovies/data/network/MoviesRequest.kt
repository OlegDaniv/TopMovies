package com.example.topmovies.data.network

import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.data.utils.unwrap
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.GetPreference
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Failure
import com.example.topmovies.presentation.models.Movie

class MoviesRequest(
    private val api: MoviesApi,
    /** Because the IMDB API has a limited number of requests (only 100), we use the mock server to test our app. You can use the IMDB API when you pass the key into the API. getMovies() function, like that api.getMovies(getApiKey()) **/
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

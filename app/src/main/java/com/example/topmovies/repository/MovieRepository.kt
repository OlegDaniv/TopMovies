package com.example.topmovies.repository

import android.content.SharedPreferences
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.models.*
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService

class MovieRepository constructor(
    private val api: MoviesApi,
    private val moviesDao: MoviesDao,
    private val movieDetailsDao: MovieDetailsDao,
    private val sharedPreferences: SharedPreferences,
    private val executor: ExecutorService
) {

    fun loadMovies(): List<MovieEntity> = moviesDao.getMovies()

    fun loadFavoriteMovie(isFavorite: Boolean) = moviesDao.getFavoriteMovies(isFavorite)

    fun loadMovieDetailsById(id: String) = movieDetailsDao.getMovieDetailsById(id)

    fun updateMovie(id: String, isFavorite: Boolean) {
        moviesDao.updateMovie(id, isFavorite)
    }

    fun upsertMovies(movies: List<Movie>) {
        moviesDao.upsertMovies(movies)
    }

    fun insert(movie: MovieDetailsEntity) {
        executor.execute { movieDetailsDao.insert(movie) }
    }

    /** for realize use getApiKey() like parameter in api.getMovies() or api.getMovieDetails(), now it's mock **/
    fun getApiKey() =
        sharedPreferences.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY)
            ?.takeIf { it.isNotBlank() }
            ?: DEF_API_KEY

    fun loadNewMovies(onSuccess: (MovieObject) -> Unit, onError: (String) -> Unit) {
        api.getMovies().enqueue(object : Callback<MovieObject> {

            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.errorMessage.isNotEmpty()) {
                            onError(it.errorMessage)
                        } else {
                            onSuccess(it)
                        }
                    }
                } else {
                    onError(response.code().toString())

                }
            }

            override fun onFailure(call: Call<MovieObject>, t: Throwable) {
                onError(t.message.orEmpty())
            }
        })
    }

    fun loadMovieDetails(
        movieId: String,
        onSuccess: (MovieDetailsApi) -> Unit,
        onError: (String) -> Unit
    ) {
        api.getMovieDetails()
            .enqueue(object : Callback<MovieDetailsApi> {
                override fun onResponse(
                    call: Call<MovieDetailsApi>,
                    response: Response<MovieDetailsApi>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.errorMessage != null) {
                                onError(it.errorMessage)
                            } else {
                                insert(it.toMovieDetailsEntity())
                                onSuccess(it)
                            }
                        }
                    } else {
                        onError(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<MovieDetailsApi>, t: Throwable) {
                    onError(t.message.orEmpty())
                }
            })
    }
}

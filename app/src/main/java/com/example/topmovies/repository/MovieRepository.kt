package com.example.topmovies.repository

import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.models.*
import com.example.topmovies.retrofit.MoviesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService

class MovieRepository constructor(
    private val api: MoviesApi,
    private val moviesDao: MoviesDao,
    private val movieDetailsDao: MovieDetailsDao,
    private val executor: ExecutorService
) {

    fun getMovies(callback: (List<MovieEntity>) -> Unit) {
        executor.execute { callback(moviesDao.getMovies()) }
    }

    fun updateMovie(id: String, boolean: Boolean) {
        executor.execute { moviesDao.updateMovie(id, boolean) }
    }

    fun getMovieDetailsById(id: String, callback: (MovieDetailsEntity?) -> Unit) {
        executor.execute { callback(movieDetailsDao.getMovieDetailsById(id)) }
    }

    fun upsertMovies(movies: List<Movie>, callback: (List<MovieEntity>) -> Unit) {
        executor.execute {
            moviesDao.upsertMovies(movies)
            getMovies(callback)
        }
    }

    private fun insert(movie: MovieDetailsEntity) {
        executor.execute { movieDetailsDao.insert(movie) }
    }

    fun getNewMovies(
        apikey: String, onSuccess: (MovieObject) -> Unit, onError: (String) -> Unit
    ) {
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

    fun getMovieDetails(
        movieId: String,
        apikey: String,
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

package com.example.topmovies.repository

import android.content.SharedPreferences
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.domain.UseCase.Data
import com.example.topmovies.models.*
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

class MovieRepository constructor(
    private val api: MoviesApi,
    private val moviesDao: MoviesDao,
    private val movieDetailsDao: MovieDetailsDao,
    private val sharedPreferences: SharedPreferences,
) {

    fun getMoviesEntity(): List<MovieEntity> = moviesDao.getMoviesEntity()

    fun getFavoriteMoviesEntity(isFavorite: Boolean) = moviesDao.getFavoriteMoviesEntity(isFavorite)

    fun getMovieDetailsEntityById(id: String) = movieDetailsDao.getMovieDetailsEntityById(id)

    fun updateMovieEntity(id: String, isFavorite: Boolean) {
        moviesDao.updateMovieEntity(id, isFavorite)
    }

    fun upsertMoviesEntity(movies: List<Movie>) {
        moviesDao.upsertMoviesEntity(movies)
    }

    fun insertMovieDetailsEntity(entity: MovieDetailsEntity) {
        movieDetailsDao.insert(entity)
    }

    /** when using api.getMovies() and api.getMovieDetails() without any parameters, it connects to a mock server.
     *  If you wish to utilize the imdb server, put the return value in api.getMovies(getApiKey()) and
     *  api.getMovieDetails(id),getApiKey()).
     *  id - movie id in the imdb server**/
    fun getApiKey() =
        sharedPreferences.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY)
            ?.takeIf { it.isNotBlank() }
            ?: DEF_API_KEY

    fun loadNewMovies(): Data<List<MovieApi>> {
        return try {
            val response = api.getMovies().execute()
            when (response.isSuccessful) {
                true -> response.body()?.items
                    ?.let { Data(it) }
                    ?: Data(emptyList(), "The list is Empty")
                false -> Data(emptyList(), response.code().toString())
            }
        } catch (e: Exception) {
            Data(emptyList(), e.message.toString())
        }
    }

    fun loadMovieDetails(id: String): Data<MovieDetails> {
        return try {
            val response = api.getMovieDetails().execute()
            when (response.isSuccessful) {
                true -> response.body()?.let { Data(it.toMovieDetails()) }
                    ?: Data(MovieDetails.empty, "Body is empty")
                false -> Data(MovieDetails.empty, response.code().toString())
            }
        } catch (e: Exception) {
            Data(MovieDetails.empty, e.message.toString())
        }
    }
}

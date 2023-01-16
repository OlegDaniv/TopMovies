package com.example.topmovies.repository

import android.content.SharedPreferences
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.models.mapper.MovieDetailsResponseMapper
import com.example.topmovies.models.mapper.MovieEntityMapper
import com.example.topmovies.models.mapper.MovieResponseMapper
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

class MovieRepository constructor(
    private val api: MoviesApi,
    private val moviesDao: MoviesDao,
    private val movieDetailsDao: MovieDetailsDao,
    private val sharedPreferences: SharedPreferences,
) {

    fun getMoviesEntity() = moviesDao.getMoviesEntity().map { MovieEntityMapper.toModel(it) }

    fun getFavoriteMoviesEntity(isFavorite: Boolean) =
        moviesDao.getFavoriteMoviesEntity(isFavorite).map { MovieEntityMapper.toModel(it) }

    fun getMovieDetailsEntityById(id: String) = movieDetailsDao.getMovieDetailsEntityById(id)
        ?.let { MovieDetailsEntityMapper.toModel(it) }

    fun updateMovieEntity(id: String, isFavorite: Boolean) {
        moviesDao.updateMovieEntity(id, isFavorite)
    }

    fun upsertMoviesEntity(movies: List<Movie>) {
        moviesDao.upsertMoviesEntity(movies.map { MovieEntityMapper.fromModel(it) })
    }

    fun insertMovieDetailsEntity(entity: MovieDetails) {
        movieDetailsDao.insert(MovieDetailsEntityMapper.fromModel(entity))
    }

    /** when using api.getMovies() and api.getMovieDetails() without any parameters, it connects to a mock server.
     *  If you wish to utilize the imdb server, put the return value in api.getMovies(getApiKey()) and
     *  api.getMovieDetails(id),getApiKey()).
     *  id - movie id in the imdb server**/
    fun getApiKey() =
        sharedPreferences.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY)
            ?.takeIf { it.isNotBlank() }
            ?: DEF_API_KEY

    fun loadNewMovies(): Result<List<Movie>> {
        return try {
            val response = api.getMovies().execute()
            when (response.isSuccessful) {
                true -> response.body()?.items?.map { MovieResponseMapper.toModel(it) }
                    ?.let { Result(it) }
                    ?: Result(emptyList(), "The list is Empty")
                false -> Result(emptyList(), response.code().toString())
            }
        } catch (e: Exception) {
            Result(emptyList(), e.message.toString())
        }
    }

    fun loadMovieDetails(id: String): Result<MovieDetails> {
        return try {
            val response = api.getMovieDetails().execute()
            when (response.isSuccessful) {
                true -> response.body()?.let { Result(MovieDetailsResponseMapper.toModel(it)) }
                    ?: Result(MovieDetails.empty, "Body is empty")
                false -> Result(MovieDetails.empty, response.code().toString())
            }
        } catch (e: Exception) {
            Result(MovieDetails.empty, e.message.toString())
        }
    }
}

package com.example.topmovies.repository

import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.Movie
import com.example.topmovies.models.MovieApi
import com.example.topmovies.models.MovieEntity
import com.example.topmovies.retrofit.MoviesApi

class MoviesRepositoryImpl constructor(
    private val api: MoviesApi,
    private val moviesDao: MoviesDao,
) : MoviesRepository {

    override fun getMovies(): List<MovieEntity> = moviesDao.getMovies()

    override fun getFavoriteMovies(isFavorite: Boolean) =
        moviesDao.getFavoriteMovies(isFavorite)

    override fun updateMovie(id: String, isFavorite: Boolean) {
        moviesDao.updateMovie(id, isFavorite)
    }

    override fun upsertMovies(movies: List<Movie>) {
        moviesDao.upsertMovies(movies)
    }

    override fun loadNewMovies(): Result<List<MovieApi>> {
        return try {
            val response = api.getMovies().execute()
            if (response.isSuccessful) {
                response.body()?.items?.let { Result(it) } ?: Result(
                    emptyList(),
                    "The list is Empty"
                )
            } else {
                Result(emptyList(), response.code().toString())
            }
        } catch (e: Exception) {
            Result(emptyList(), e.message.toString())
        }
    }
}

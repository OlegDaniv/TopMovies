package com.example.topmovies.data.repository

import com.example.topmovies.data.dao.MoviesDao
import com.example.topmovies.data.network.MoviesRequest
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.Movie

interface MoviesRepository {

    fun getMovies(): Result<Failure, List<Movie>>

    fun getFavoriteMovies(): Result<Failure, List<Movie>>

    fun loadNewMovie(): Result<Failure, List<Movie>>

    fun updateMovie(params: Params)

    class MoviesRepositoryImpl(
        private val moviesDao: MoviesDao,
        private val movieRequest: MoviesRequest,
    ) : MoviesRepository {

        override fun getMovies(): Result<Failure, List<Movie>> {
            val movies = moviesDao.getMovies().map { it.toMovie() }
            return if (movies.isEmpty()) {
                loadNewMovie()
            } else {
                Success(movies)
            }
        }

        override fun loadNewMovie(): Result<Failure, List<Movie>> {
            val newMovies = movieRequest.getNewMovies()
            newMovies.asSuccess().let { moviesDao.upsertMovies(it.result) }
            return newMovies
        }

        override fun getFavoriteMovies(): Result<Failure, List<Movie>> =
            Success(moviesDao.getFavoriteMovies(true).map { it.toMovie() })

        override fun updateMovie(params: Params) {
            moviesDao.updateMovie(params.id, params.isFavorite)
        }
    }
}

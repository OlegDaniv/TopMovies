package com.example.topmovies.data.repository

import com.example.topmovies.data.dao.MoviesDao
import com.example.topmovies.data.network.MoviesRequest
import com.example.topmovies.domain.exeption.Failure
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.domain.utils.ResultOf.Success
import com.example.topmovies.presentation.models.Movie

interface MoviesRepository {

    fun getMoviesEntity(): ResultOf<Failure, List<Movie>>

    fun getFavoriteMoviesEntity(): ResultOf<Failure, List<Movie>>

    fun loadNewMovie(): ResultOf<Failure, List<Movie>>

    fun updateMovieEntity(params: Params)

    class MoviesRepositoryImp(
        private val moviesDao: MoviesDao,
        private val movieRequest: MoviesRequest,
    ) : MoviesRepository {

        override fun getMoviesEntity(): ResultOf<Failure, List<Movie>> {
            val movies = moviesDao.getMoviesEntity().map { it.toMovie() }
            return if (movies.isEmpty()) {
                loadNewMovie()
            } else {
               Success(movies)
            }
        }

        override fun loadNewMovie(): ResultOf<Failure, List<Movie>> {
            val newMovies = movieRequest.getNewMovies()
            moviesDao.upsertMoviesEntity(newMovies)
            return newMovies
        }

        override fun getFavoriteMoviesEntity(): ResultOf<Nothing, List<Movie>> =
            Success(moviesDao.getFavoriteMoviesEntity(true).map { it.toMovie() })

        override fun updateMovieEntity(params: Params) {
            moviesDao.updateMovieEntity(params.id, params.isFavorite)
        }
    }
}

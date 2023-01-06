package com.example.topmovies.data.repository

import com.example.topmovies.data.dao.MoviesDao
import com.example.topmovies.data.network.MoviesRequest
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.domain.utils.ResultOf.Success
import com.example.topmovies.presentation.models.Movie

interface MoviesRepository {

    fun getMoviesEntity(): ResultOf<Failure, List<Movie>>

    fun getFavoriteMovies(): ResultOf<Failure, List<Movie>>

    fun loadNewMovie(): ResultOf<Failure, List<Movie>>

    fun updateMovie(params: Params)

    class MoviesRepositoryImp(
        private val moviesDao: MoviesDao,
        private val movieRequest: MoviesRequest,
    ) : MoviesRepository {

        override fun getMoviesEntity(): ResultOf<Failure, List<Movie>> {
            val movies = moviesDao.getMovies().map { it.toMovie() }
            return if (movies.isEmpty()) {
                loadNewMovie()
            } else {
               Success(movies)
            }
        }

        override fun loadNewMovie(): ResultOf<Failure, List<Movie>> {
            val newMovies = movieRequest.getNewMovies()
            newMovies.fold(
                onFailed = {},
                onSuccess = { moviesDao.upsertMovies(it) }
            )
            return newMovies
        }

        override fun getFavoriteMovies(): ResultOf<Nothing, List<Movie>> =
            Success(moviesDao.getFavoriteMovies(true).map { it.toMovie() })

        override fun updateMovie(params: Params) {
            moviesDao.updateMovie(params.id, params.isFavorite)
        }
    }
}

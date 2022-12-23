package com.example.topmovies.datalayer.repository

import com.example.topmovies.datalayer.dao.MoviesDao
import com.example.topmovies.datalayer.network.MoviesRequest
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf
import com.example.topmovies.utils.ResultOf.Success


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

    data class Movie(
        val id: String,
        val rank: String,
        val rankUpDown: String,
        val title: String,
        val fullTitle: String,
        val year: String,
        val imageUrl: String,
        val crew: String,
        val imDbRating: String,
        val imDbRatingCount: String,
        var isFavorite: Boolean = false
    ) {
        fun toMovieEntity(): MoviesDao.MovieEntity {
            return MoviesDao.MovieEntity(
                id, rank, rankUpDown, title, fullTitle,
                year, imageUrl, crew, imDbRating, imDbRatingCount, isFavorite
            )
        }
    }
}

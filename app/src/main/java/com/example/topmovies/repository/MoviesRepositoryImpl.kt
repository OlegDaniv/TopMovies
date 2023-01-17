package com.example.topmovies.repository

import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.mapper.MovieEntityMapper
import com.example.topmovies.retrofit.MoviesRequest
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result

class MoviesRepositoryImpl constructor(
    private val moviesDao: MoviesDao,
    private val movieRequest: MoviesRequest
) : MoviesRepository {

    override fun getMovies(): List<Movie> =
        moviesDao.getMovies().map { MovieEntityMapper.toModel(it) }

    override fun getFavoriteMovies(isFavorite: Boolean) =
        moviesDao.getFavoriteMovies(isFavorite).map { MovieEntityMapper.toModel(it) }

    override fun updateMovie(id: String, isFavorite: Boolean) {
        moviesDao.updateMovie(id, isFavorite)
    }

    override fun upsertMovies(movies: List<Movie>) {
        moviesDao.upsertMovies(movies.map { MovieEntityMapper.fromModel(it) })
    }

    override fun loadNewMovies(): Result<Error, List<Movie>> {
        val newMovies = movieRequest.loadNewMovies()
        newMovies.process { movies ->
            moviesDao.upsertMovies(
                movies.map { MovieEntityMapper.fromModel(it) }
            )
        }
        return newMovies
    }
}

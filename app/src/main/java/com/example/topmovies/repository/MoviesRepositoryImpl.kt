package com.example.topmovies.repository

import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.mapper.MovieEntityMapper
import com.example.topmovies.retrofit.MoviesRequest
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Success

class MoviesRepositoryImpl constructor(
    private val moviesDao: MoviesDao, private val movieRequest: MoviesRequest
) : MoviesRepository {

    override fun getMovies(): Result<Error, List<Movie>> {
        val movies = moviesDao.getMovies().map { MovieEntityMapper.toModel(it) }
        return if (movies.isEmpty()) {
            loadNewMovies()
        } else {
            Success(movies)
        }
    }

    override fun getFavoriteMovies() =
        moviesDao.getFavoriteMovies().map { MovieEntityMapper.toModel(it) }

    override fun updateMovie(id: String, isFavorite: Boolean) {
        moviesDao.updateMovie(id, isFavorite)
    }

    override fun upsertMovies(movies: List<Movie>) {
        moviesDao.upsertMovies(movies.map { MovieEntityMapper.fromModel(it) })
    }

    override fun loadNewMovies(): Result<Error, List<Movie>> {
        val newMovies = movieRequest.loadNewMovies()
        return if (newMovies is Success) {
            moviesDao.upsertMovies(newMovies.result.map { MovieEntityMapper.fromModel(it) })
            Success(moviesDao.getMovies().map { MovieEntityMapper.toModel(it) })
        } else {
            newMovies
        }
    }
}

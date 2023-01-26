package com.example.topmovies.data.repositores

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Success
import com.example.topmovies.data.database.dao.MoviesDao
import com.example.topmovies.data.mappers.MovieEntityMapper
import com.example.topmovies.data.network.requests.MoviesRequest

class MoviesRepositoryImpl constructor(
    private val moviesDao: MoviesDao,
    private val movieRequest: MoviesRequest
) : MoviesRepository {

    override suspend fun getMovies(): Result<Error, List<Movie>> {
        val movies = moviesDao.getMovies().map { MovieEntityMapper.toModel(it) }
        return if (movies.isEmpty()) {
            loadNewMovies()
        } else {
            Success(movies)
        }
    }

    override suspend fun getFavoriteMovies() =
        moviesDao.getFavoriteMovies().map { MovieEntityMapper.toModel(it) }

    override suspend fun updateMovie(id: String, isFavorite: Boolean) {
        moviesDao.updateMovie(id, isFavorite)
    }

    override suspend fun upsertMovies(movies: List<Movie>) {
        moviesDao.upsertMovies(movies.map { MovieEntityMapper.fromModel(it) })
    }

    override suspend fun loadNewMovies(): Result<Error, List<Movie>> {
        val newMovies = movieRequest.loadNewMovies()
        return if (newMovies is Success) {
            moviesDao.upsertMovies(newMovies.data.map { MovieEntityMapper.fromModel(it) })
            Success(moviesDao.getMovies().map { MovieEntityMapper.toModel(it) })
        } else {
            newMovies
        }
    }
}

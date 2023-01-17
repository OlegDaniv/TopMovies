package com.example.topmovies.repository

import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.mapper.MovieEntityMapper
import com.example.topmovies.models.mapper.MovieResponseMapper
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Error.ServerError
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Failure
import com.example.topmovies.utils.Result.Success

class MoviesRepositoryImpl constructor(
    private val api: MoviesApi,
    private val moviesDao: MoviesDao,
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
        return try {
            val response = api.getMovies().execute()
            if (response.isSuccessful) {
                response.body()?.items?.map { MovieResponseMapper.toModel(it) }
                    ?.let { Success(it) }
                    ?: Failure(ServerError)
            } else {
                Failure(ServerError)
            }
        } catch (e: Exception) {
            Failure(ServerError)
        }
    }
}

package com.example.topmovies.repository

import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.mapper.MovieEntityMapper
import com.example.topmovies.models.mapper.MovieResponseMapper
import com.example.topmovies.retrofit.MoviesApi

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

    override fun loadNewMovies(): Result<List<Movie>> {
        return try {
            val response = api.getMovies().execute()
            if (response.isSuccessful) {
                response.body()?.items?.map { MovieResponseMapper.toModel(it) }
                    ?.let { Result(it) }
                    ?: Result(emptyList(), "The list is Empty")
            } else {
                Result(emptyList(), response.code().toString())
            }
        } catch (e: Exception) {
            Result(emptyList(), e.message.toString())
        }
    }
}

package com.example.topmovies.repository

import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.models.mapper.MovieDetailsResponseMapper
import com.example.topmovies.retrofit.MoviesApi

class MovieDetailsRepositoryImpl(
    private val api: MoviesApi,
    private val movieDetailsDao: MovieDetailsDao,
) : MovieDetailsRepository {

    override fun getMovieDetails(id: String): MovieDetails? = movieDetailsDao.getMovieDetails(id)
        ?.let { MovieDetailsEntityMapper.toModel(it) }

    override fun insertMovieDetails(entity: MovieDetails) {
        movieDetailsDao.insert(MovieDetailsEntityMapper.fromModel(entity))
    }

    override fun loadNewMovieDetails(id: String): Result<MovieDetails> {
        return try {
            val response = api.getMovieDetails().execute()
            if (response.isSuccessful) {
                response.body()?.let { Result(MovieDetailsResponseMapper.toModel(it)) }
                    ?: Result(MovieDetails.empty, "Body is empty")
            } else {
                Result(MovieDetails.empty, response.code().toString())
            }
        } catch (e: Exception) {
            Result(MovieDetails.empty, e.message.toString())
        }
    }
}

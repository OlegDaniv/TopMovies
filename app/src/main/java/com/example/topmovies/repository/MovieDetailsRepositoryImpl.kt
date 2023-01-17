package com.example.topmovies.repository

import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.models.mapper.MovieDetailsResponseMapper
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Error.ServerError
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Failure
import com.example.topmovies.utils.Result.Success
import java.io.IOException

class MovieDetailsRepositoryImpl(
    private val api: MoviesApi,
    private val movieDetailsDao: MovieDetailsDao,
) : MovieDetailsRepository {

    override fun getMovieDetails(id: String): MovieDetails? = movieDetailsDao.getMovieDetails(id)
        ?.let { MovieDetailsEntityMapper.toModel(it) }

    override fun insertMovieDetails(entity: MovieDetails) {
        movieDetailsDao.insert(MovieDetailsEntityMapper.fromModel(entity))
    }

    override fun loadNewMovieDetails(id: String): Result<Error, MovieDetails> {
        return try {
            val response = api.getMovieDetails().execute()
            if (response.isSuccessful) {
                response.body()?.let { Success(MovieDetailsResponseMapper.toModel(it)) }
                    ?: Failure(ServerError)
            } else {
                Failure(ServerError)
            }
        } catch (e: IOException) {
            Failure(ServerError)
        }
    }
}

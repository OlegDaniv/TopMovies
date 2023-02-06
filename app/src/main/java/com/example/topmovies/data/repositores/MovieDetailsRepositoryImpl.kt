package com.example.topmovies.data.repositores

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Success
import com.example.topmovies.data.database.dao.MovieDetailsDao
import com.example.topmovies.data.mappers.MovieDetailsEntityMapper
import com.example.topmovies.data.network.requests.MovieDetailsRequest

class MovieDetailsRepositoryImpl(
    private val movieDetailsDao: MovieDetailsDao,
    private val movieDetailsRequest: MovieDetailsRequest
) : MovieDetailsRepository {

    override suspend fun getMovieDetails(id: String): Result<Error, MovieDetails> {
        val movieDetails = movieDetailsDao.getMovieDetails(id)
        return movieDetails?.let { Success(MovieDetailsEntityMapper.toModel(it)) }
            ?: loadNewMovieDetails(id)
    }

    override suspend fun loadNewMovieDetails(id: String): Result<Error, MovieDetails> {
        val newMovieDetails = movieDetailsRequest.loadNewMovieDetails()
        return if (newMovieDetails is Success) {
            movieDetailsDao.insertMovieDetails(MovieDetailsEntityMapper.fromModel(newMovieDetails.data))
            Success(newMovieDetails.data)
        } else {
            newMovieDetails
        }
    }
}

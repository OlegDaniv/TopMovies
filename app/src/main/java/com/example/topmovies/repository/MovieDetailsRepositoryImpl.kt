package com.example.topmovies.repository

import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.retrofit.MovieDetailsRequest
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Success

class MovieDetailsRepositoryImpl(
    private val movieDetailsDao: MovieDetailsDao,
    private val movieDetailsRequest: MovieDetailsRequest
) : MovieDetailsRepository {

    override fun getMovieDetails(id: String): Result<Error, MovieDetails> {
        val movieDetails = movieDetailsDao.getMovieDetails(id)
        return movieDetails?.let { Success(MovieDetailsEntityMapper.toModel(it)) }
            ?: loadNewMovieDetails(id)
    }

    override fun loadNewMovieDetails(id: String): Result<Error, MovieDetails> {
        val newMovieDetails = movieDetailsRequest.loadNewMovieDetails()
        newMovieDetails.process {
            movieDetailsDao.insertMovieDetails(MovieDetailsEntityMapper.fromModel(it))
        }
        return newMovieDetails
    }
}

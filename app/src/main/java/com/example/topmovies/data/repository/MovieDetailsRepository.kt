package com.example.topmovies.data.repository

import com.example.topmovies.data.dao.MovieDetailsDao
import com.example.topmovies.data.network.MovieDetailsRequest
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.MovieDetails

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): Result<Error, MovieDetails>

    fun loadNewMovieDetailsById(id: String): Result<Error, MovieDetails>

    class MovieDetailsRepositoryImpl(
        private val movieDetailsDao: MovieDetailsDao,
        private val movieDetailsRequest: MovieDetailsRequest
    ) : MovieDetailsRepository {

        override fun getMovieDetails(id: String): Result<Error, MovieDetails> {
            val movieDetails = movieDetailsDao.getMovieDetailsById(id)
            return if (movieDetails == null) {
                 loadNewMovieDetailsById(id)
            } else {
                Success(movieDetails.toMovieDetails())
            }
        }

        override fun loadNewMovieDetailsById(id: String): Result<Error, MovieDetails> {
            val newMovieDetails = movieDetailsRequest.loadMovieDetails(id)
            movieDetailsDao.insertMovieDetails(newMovieDetails.asSuccess().result.toMovieDetailsEntity())
            return newMovieDetails
        }
    }
}

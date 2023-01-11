package com.example.topmovies.data.repository

import com.example.topmovies.data.dao.MovieDetailsDao
import com.example.topmovies.data.network.MovieDetailsRequest
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.MovieDetails

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): Result<Failure, MovieDetails>

    fun loadNewMovieDetailsById(id: String): Result<Failure, MovieDetails>

    class MovieDetailsRepositoryImp(
        private val movieDetailsDao: MovieDetailsDao,
        private val movieDetailsRequest: MovieDetailsRequest
    ) : MovieDetailsRepository {

        override fun getMovieDetails(id: String): Result<Failure, MovieDetails> {
            val movieDetails = movieDetailsDao.getMovieDetailsById(id)
            return if (movieDetails == null) {
                 loadNewMovieDetailsById(id)
            } else {
                Success(movieDetails.toMovieDetails())
            }
        }

        override fun loadNewMovieDetailsById(id: String): Result<Failure, MovieDetails> {
            val newMovieDetails = movieDetailsRequest.loadMovieDetails(id)
            movieDetailsDao.insertMovieDetails(newMovieDetails.asSuccess().result.toMovieDetailsEntity())
            return newMovieDetails
        }
    }
}

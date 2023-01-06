package com.example.topmovies.data.repository

import com.example.topmovies.data.dao.MovieDetailsDao
import com.example.topmovies.data.network.MovieDetailsRequest
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.domain.utils.ResultOf.Failed
import com.example.topmovies.domain.utils.ResultOf.Success
import com.example.topmovies.presentation.models.MovieDetails

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): ResultOf<Failure, MovieDetails>

    fun loadNewMovieDetailsById(id: String): ResultOf<Failure, MovieDetails>

    class MovieDetailsRepositoryImp(
        private val movieDetailsDao: MovieDetailsDao,
        private val movieDetailsRequest: MovieDetailsRequest
    ) : MovieDetailsRepository {

        override fun getMovieDetails(id: String): ResultOf<Failure, MovieDetails> {
            val movieDetails = movieDetailsDao.getMovieDetailsById(id)
            return if (movieDetails == null) {
                val newMovieDetails = loadNewMovieDetailsById(id)
                newMovieDetails.fold(
                    onFailed = { Failed(it) },
                    onSuccess = { movieDetailsDao.insertMovieDetails(it.toMovieDetailsEntity()) }
                )
                newMovieDetails
            } else {
                Success(movieDetails.toMovieDetails())
            }
        }

        override fun loadNewMovieDetailsById(id: String): ResultOf<Failure, MovieDetails> {
            return movieDetailsRequest.loadMovieDetails(id)
        }
    }
}

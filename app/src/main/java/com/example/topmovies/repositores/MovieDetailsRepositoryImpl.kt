package com.example.topmovies.repositores

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Success
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.retrofit.MovieDetailsRequest

class MovieDetailsRepositoryImpl(
    private val movieDetailsDao: MovieDetailsDao,
    private val movieDetailsRequest: MovieDetailsRequest
) : MovieDetailsRepository {

    override fun getMovieDetails(id: String): Result<Error, MovieDetails> {
        val movieDetails = movieDetailsDao.getMovieDetails(id)
        return movieDetails?.let {
            Success(
                MovieDetailsEntityMapper.toModel(
                    it
                )
            )
        }
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

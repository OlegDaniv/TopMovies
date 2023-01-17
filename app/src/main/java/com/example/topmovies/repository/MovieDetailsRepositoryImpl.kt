package com.example.topmovies.repository

import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.retrofit.MovieDetailsRequest
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result

class MovieDetailsRepositoryImpl(
    private val movieDetailsDao: MovieDetailsDao,
    private val movieDetailsRequest: MovieDetailsRequest
) : MovieDetailsRepository {

    override fun getMovieDetails(id: String): MovieDetails? = movieDetailsDao.getMovieDetails(id)
        ?.let { MovieDetailsEntityMapper.toModel(it) }

    override fun insertMovieDetails(entity: MovieDetails) {
        movieDetailsDao.insert(MovieDetailsEntityMapper.fromModel(entity))
    }

    override fun loadNewMovieDetails(id: String): Result<Error, MovieDetails> {
        val newMovieDetails = movieDetailsRequest.loadNewMovieDetails()
        newMovieDetails.process {
            movieDetailsDao.insert(MovieDetailsEntityMapper.fromModel(it))
        }
        return newMovieDetails
    }
}

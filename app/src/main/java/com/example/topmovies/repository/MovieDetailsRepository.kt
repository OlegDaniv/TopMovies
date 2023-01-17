package com.example.topmovies.repository

import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): MovieDetails?

    fun insertMovieDetails(entity: MovieDetails)

    fun loadNewMovieDetails(id: String): Result<Error, MovieDetails>
}

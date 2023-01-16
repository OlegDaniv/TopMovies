package com.example.topmovies.repository

import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.MovieDetails
import com.example.topmovies.models.MovieDetailsEntity

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): MovieDetailsEntity?

    fun insertMovieDetails(entity: MovieDetailsEntity)

    fun loadNewMovieDetails(id: String): Result<MovieDetails>
}

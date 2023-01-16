package com.example.topmovies.repository

import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.domain.MovieDetails

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): MovieDetails?

    fun insertMovieDetails(entity: MovieDetails)

    fun loadNewMovieDetails(id: String): Result<MovieDetails>
}

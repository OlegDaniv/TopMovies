package com.example.domain.repositores

import com.example.domain.models.MovieDetails
import com.example.domain.utils.Error
import com.example.domain.utils.Result

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): Result<Error, MovieDetails>

    fun loadNewMovieDetails(id: String): Result<Error, MovieDetails>
}

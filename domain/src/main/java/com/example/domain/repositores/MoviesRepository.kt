package com.example.domain.repositores

import com.example.domain.models.Movie
import com.example.domain.utils.Error
import com.example.domain.utils.Result

interface MoviesRepository {

    suspend fun getMovies(): Result<Error, List<Movie>>

    suspend fun getFavoriteMovies(): List<Movie>

    suspend fun upsertMovies(movies: List<Movie>)

    suspend fun updateMovie(id: String, isFavorite: Boolean)

    suspend fun loadNewMovies(): Result<Error, List<Movie>>
}

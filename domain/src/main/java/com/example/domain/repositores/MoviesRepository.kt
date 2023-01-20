package com.example.domain.repositores

import com.example.domain.models.Movie
import com.example.domain.utils.Error
import com.example.domain.utils.Result

interface MoviesRepository {

    fun getMovies(): Result<Error, List<Movie>>

    fun getFavoriteMovies(): List<Movie>

    fun upsertMovies(movies: List<Movie>)

    fun updateMovie(id: String, isFavorite: Boolean)

    fun loadNewMovies(): Result<Error, List<Movie>>
}

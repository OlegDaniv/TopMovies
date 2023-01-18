package com.example.topmovies.repository

import com.example.topmovies.models.domain.Movie
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result

interface MoviesRepository {

    fun getMovies(): Result<Error, List<Movie>>

    fun getFavoriteMovies(): List<Movie>

    fun upsertMovies(movies: List<Movie>)

    fun updateMovie(id: String, isFavorite: Boolean)

    fun loadNewMovies(): Result<Error, List<Movie>>
}

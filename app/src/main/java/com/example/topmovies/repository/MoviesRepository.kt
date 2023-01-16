package com.example.topmovies.repository

import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.domain.Movie

interface MoviesRepository {

    fun getMovies(): List<Movie>

    fun getFavoriteMovies(isFavorite: Boolean): List<Movie>

    fun upsertMovies(movies: List<Movie>)

    fun updateMovie(id: String, isFavorite: Boolean)

    fun loadNewMovies(): Result<List<Movie>>
}

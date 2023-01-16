package com.example.topmovies.repository

import com.example.topmovies.domain.UseCase
import com.example.topmovies.models.Movie
import com.example.topmovies.models.MovieApi
import com.example.topmovies.models.MovieEntity

interface MoviesRepository {

    fun getMovies(): List<MovieEntity>

    fun getFavoriteMovies(isFavorite: Boolean): List<MovieEntity>

    fun upsertMovies(movies: List<Movie>)

    fun updateMovie(id: String, isFavorite: Boolean)

    fun loadNewMovies(): UseCase.Result<List<MovieApi>>
}

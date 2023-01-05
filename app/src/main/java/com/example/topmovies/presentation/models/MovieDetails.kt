package com.example.topmovies.presentation.models

import com.example.topmovies.data.models.entity.MovieDetailsEntity

data class MovieDetails(
    val id: String,
    val title: String,
    val year: String,
    val plot: String,
    val imageUrl: String,
    val releaseDate: String,
    val runtimeStr: String,
    val genres: String,
    val imDbRating: String,
    val errorMessage: String? = null
) {

    fun toMovieDetailsEntity(): MovieDetailsEntity {
        return MovieDetailsEntity(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

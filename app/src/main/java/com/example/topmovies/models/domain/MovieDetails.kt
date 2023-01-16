package com.example.topmovies.models.domain

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
    companion object {
        val empty = MovieDetails(
            "", "", "", "", "", "",
            "", "", "", null
        )
    }
}

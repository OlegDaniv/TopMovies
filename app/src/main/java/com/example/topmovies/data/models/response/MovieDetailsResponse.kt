package com.example.topmovies.data.models.response

import com.example.topmovies.data.repository.MovieDetailsRepository.*
import com.example.topmovies.presentation.models.MovieDetails
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    val id: String,
    val title: String,
    val year: String,
    val plot: String,
    @SerializedName("image")
    val imageUrl: String,
    val releaseDate: String,
    val runtimeStr: String,
    val genres: String,
    val imDbRating: String,
    val errorMessage: String? = null
) {

    fun toMovieDetails(): MovieDetails {
        return MovieDetails(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

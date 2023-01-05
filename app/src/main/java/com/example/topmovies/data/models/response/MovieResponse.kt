package com.example.topmovies.data.models.response

import com.example.topmovies.data.repository.MoviesRepository.*
import com.example.topmovies.presentation.models.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val id: String,
    val rank: String,
    val rankUpDown: String,
    val title: String,
    val fullTitle: String,
    val year: String,
    @SerializedName("image")
    val imageUrl: String,
    val crew: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    var isFavorite: Boolean = false
) {

    fun toMovie(): Movie {
        return Movie(
            id, rank, rankUpDown, title, fullTitle, year, imageUrl,
            crew, imDbRating, imDbRatingCount, isFavorite
        )
    }
}

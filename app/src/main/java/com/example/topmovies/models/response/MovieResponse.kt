package com.example.topmovies.models.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val id: String,
    val rank: String,
    val rankUpDown: String,
    val title: String,
    val fullTitle: String,
    val year: String,
    @SerializedName("image") val imageUrl: String,
    val crew: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    var isFavorite: Boolean = false
)

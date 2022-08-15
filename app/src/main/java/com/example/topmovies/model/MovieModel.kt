package com.example.topmovies.model

import com.google.gson.annotations.SerializedName

data class MovieObject(
    val items: List<Movie>
)

data class Movie(
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
    val imDbRatingCount: String
)

data class MovieDetailed(
    val title: String,
    val year: String,
    val plot: String,
    @SerializedName("image")
    val imageUrl: String,
    val releaseDate: String,
    val runtimeStr: String,
    val genres: String,
    val imDbRating: String,
)

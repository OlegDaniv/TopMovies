package com.example.topmovies.data.network.models

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
    val errorMessage: String?
)

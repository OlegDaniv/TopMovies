package com.example.topmovies.data.network.models

data class MovieObjectResponse(
    val items: List<MovieResponse>, val errorMessage: String
)

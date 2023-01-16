package com.example.topmovies.models.response

data class MovieObjectResponse(
    val items: List<MovieResponse>, val errorMessage: String
)

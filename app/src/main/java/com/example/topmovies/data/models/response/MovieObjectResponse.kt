package com.example.topmovies.data.models.response

data class MovieObjectResponse(
    val items: List<MovieResponse>, val errorMessage: String
)

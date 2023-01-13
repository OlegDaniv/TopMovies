package com.example.topmovies.models.domain

import com.example.topmovies.models.entity.MovieEntity

data class Movie(
    val id: String,
    val rank: String,
    val rankUpDown: String,
    val title: String,
    val fullTitle: String,
    val year: String,
    val imageUrl: String,
    val crew: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    var isFavorite: Boolean = false
)

fun Movie.toMovieEntity() = MovieEntity(
    id, rank, rankUpDown, title, fullTitle,
    year, imageUrl, crew, imDbRating,
    imDbRatingCount, isFavorite
)

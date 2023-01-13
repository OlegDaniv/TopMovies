package com.example.topmovies.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.topmovies.models.domain.Movie

@Entity(tableName = "tb_movies")
data class MovieEntity @JvmOverloads constructor(
    @PrimaryKey val id: String,
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

fun MovieEntity.toMovie() = Movie(
    id, rank, rankUpDown, title, fullTitle,
    year, imageUrl, crew, imDbRating,
    imDbRatingCount, isFavorite
)

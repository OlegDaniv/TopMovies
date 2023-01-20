package com.example.topmovies.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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

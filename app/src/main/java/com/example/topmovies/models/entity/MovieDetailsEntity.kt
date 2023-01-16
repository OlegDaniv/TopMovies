package com.example.topmovies.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_movie_details")
data class MovieDetailsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val year: String,
    val plot: String,
    val imageUrl: String,
    val releaseDate: String,
    val runtimeStr: String,
    val genres: String,
    val imDbRating: String,
    val errorMessage: String? = null
)

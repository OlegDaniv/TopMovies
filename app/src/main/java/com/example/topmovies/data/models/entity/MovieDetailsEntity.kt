package com.example.topmovies.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.topmovies.presentation.models.MovieDetails

@Entity(tableName = "movie_details")
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
) {

    fun toMovieDetails(): MovieDetails {
        return MovieDetails(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

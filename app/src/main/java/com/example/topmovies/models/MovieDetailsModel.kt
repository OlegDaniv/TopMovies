package com.example.topmovies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class MovieDetails(
    val id: String,
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
    companion object {
        val empty = MovieDetails(
            "", "", "", "", "", "",
            "", "", "", null
        )
    }

    fun toMovieDetailsEntity(): MovieDetailsEntity {
        return MovieDetailsEntity(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

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
) {

    fun toMovieDetails(): MovieDetails {
        return MovieDetails(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

data class MovieDetailsApi(
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
) {

    fun toMovieDetails(): MovieDetails {
        return MovieDetails(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

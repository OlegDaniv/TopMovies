package com.example.topmovies.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieObject(
    val items: List<Movie>,
    val errorMessage: String
)

data class Movie(
    val id: String,
    val rank: String,
    val rankUpDown: String,
    val title: String,
    val fullTitle: String,
    val year: String,
    @SerializedName("image")
    val imageUrl: String,
    val crew: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    var isFavorite: Boolean = false
) {
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            id,
            rank,
            rankUpDown,
            title,
            fullTitle,
            year,
            imageUrl,
            crew,
            imDbRating,
            imDbRatingCount,
            isFavorite
        )
    }
}

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

data class MovieDetails(
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
    val errorMessage: String? = null
) {
    fun toMovieDetailsEntity(): MovieDetailsEntity {
        return MovieDetailsEntity(
            id,
            title,
            year,
            plot,
            imageUrl,
            releaseDate,
            runtimeStr,
            genres,
            imDbRating
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
)

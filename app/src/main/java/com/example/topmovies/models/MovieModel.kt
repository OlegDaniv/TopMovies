package com.example.topmovies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieObject(
    val items: List<MovieApi>,
    val errorMessage: String
)

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
) {
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            id, rank, rankUpDown, title, fullTitle,
            year, imageUrl, crew, imDbRating, imDbRatingCount, isFavorite
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
) {
    fun toMovie(): Movie {
        return Movie(
            id, rank, rankUpDown, title, fullTitle,
            year, imageUrl, crew, imDbRating, imDbRatingCount, isFavorite
        )
    }
}

data class MovieApi(
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
    fun toMovie(): Movie {
        return Movie(
            id, rank, rankUpDown, title, fullTitle, year, imageUrl,
            crew, imDbRating, imDbRatingCount, isFavorite
        )
    }
}

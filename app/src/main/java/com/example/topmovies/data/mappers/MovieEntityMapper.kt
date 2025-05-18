package com.example.topmovies.data.mappers

import com.example.domain.models.Movie
import com.example.topmovies.data.database.models.MovieEntity

class MovieEntityMapper : Mapper<MovieEntity, Movie> {

    override fun toModel(value: MovieEntity) = with(value) {
        Movie(
            id = id,
            rank = rank,
            rankUpDown = rankUpDown,
            title = title,
            fullTitle = fullTitle,
            year = year,
            imageUrl = imageUrl,
            crew = crew,
            imDbRating = imDbRating,
            imDbRatingCount = imDbRatingCount,
            isFavorite = isFavorite
        )
    }

    override fun fromModel(value: Movie) = with(value) {
        MovieEntity(
            id = id,
            rank = rank,
            rankUpDown = rankUpDown,
            title = title,
            fullTitle = fullTitle,
            year = year,
            imageUrl = imageUrl,
            crew = crew,
            imDbRating = imDbRating,
            imDbRatingCount = imDbRatingCount,
            isFavorite = isFavorite
        )
    }
}

package com.example.topmovies.data.mappers

import com.example.domain.models.Movie
import com.example.topmovies.data.network.models.MovieResponse

class MovieResponseMapper : Mapper<MovieResponse, Movie> {

    override fun toModel(value: MovieResponse) = with(value) {
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
        MovieResponse(
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

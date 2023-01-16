package com.example.topmovies.models.mapper

import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.response.MovieResponse

class MovieResponseMapper : Mapper<MovieResponse, Movie> {

    override fun toModel(value: MovieResponse) = with(value) {
        Movie(
            id, rank, rankUpDown, title, fullTitle, year, imageUrl,
            crew, imDbRating, imDbRatingCount, isFavorite
        )
    }

    override fun fromModel(value: Movie) = with(value) {
        MovieResponse(
            id, rank, rankUpDown, title, fullTitle, year, imageUrl,
            crew, imDbRating, imDbRatingCount, isFavorite
        )
    }
}

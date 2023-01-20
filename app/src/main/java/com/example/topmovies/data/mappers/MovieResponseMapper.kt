package com.example.topmovies.data.mappers

import com.example.domain.models.Movie
import com.example.topmovies.data.network.models.MovieResponse

object MovieResponseMapper : Mapper<MovieResponse, Movie> {

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

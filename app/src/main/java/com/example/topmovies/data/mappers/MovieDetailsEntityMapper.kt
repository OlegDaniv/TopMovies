package com.example.topmovies.data.mappers

import com.example.domain.models.MovieDetails
import com.example.topmovies.data.database.models.MovieDetailsEntity

object MovieDetailsEntityMapper : Mapper<MovieDetailsEntity, MovieDetails> {

    override fun toModel(value: MovieDetailsEntity) = with(value) {
        MovieDetails(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }

    override fun fromModel(value: MovieDetails) = with(value) {
        MovieDetailsEntity(
            id, title, year, plot, imageUrl, releaseDate,
            runtimeStr, genres, imDbRating, errorMessage
        )
    }
}

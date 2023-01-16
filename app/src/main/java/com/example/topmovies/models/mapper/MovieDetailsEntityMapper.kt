package com.example.topmovies.models.mapper

import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.entity.MovieDetailsEntity

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

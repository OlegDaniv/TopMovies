package com.example.topmovies.data.mappers

import com.example.domain.models.MovieDetails
import com.example.topmovies.data.database.models.MovieDetailsEntity

class MovieDetailsEntityMapper : Mapper<MovieDetailsEntity, MovieDetails> {

    override fun toModel(value: MovieDetailsEntity) = with(value) {
        MovieDetails(
            id = id,
            title = title,
            year = year,
            plot = plot,
            imageUrl = imageUrl,
            releaseDate = releaseDate,
            runtimeStr = runtimeStr,
            genres = genres,
            imDbRating = imDbRating,
            errorMessage = errorMessage
        )
    }

    override fun fromModel(value: MovieDetails) = with(value) {
        MovieDetailsEntity(
            id = id,
            title = title,
            year = year,
            plot = plot,
            imageUrl = imageUrl,
            releaseDate = releaseDate,
            runtimeStr = runtimeStr,
            genres = genres,
            imDbRating = imDbRating,
            errorMessage = errorMessage
        )
    }
}

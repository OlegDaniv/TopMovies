package com.example.topmovies.data.mappers

import com.example.domain.models.MovieDetails
import com.example.topmovies.data.network.models.MovieDetailsResponse

class MovieDetailsResponseMapper : Mapper<MovieDetailsResponse, MovieDetails> {

    override fun toModel(value: MovieDetailsResponse) = with(value) {
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
        MovieDetailsResponse(
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

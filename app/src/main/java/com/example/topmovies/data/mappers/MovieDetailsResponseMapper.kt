package com.example.topmovies.data.mappers

import com.example.domain.models.MovieDetails
import com.example.topmovies.data.network.models.MovieDetailsResponse

object MovieDetailsResponseMapper : Mapper<MovieDetailsResponse, MovieDetails> {

    override fun toModel(value: MovieDetailsResponse) = with(value) {
        MovieDetails(
            id, title, year, plot, imageUrl, releaseDate, runtimeStr,
            genres, imDbRating, errorMessage
        )
    }

    override fun fromModel(value: MovieDetails) = with(value) {
        MovieDetailsResponse(
            id, title, year, plot, imageUrl, releaseDate, runtimeStr,
            genres, imDbRating, errorMessage
        )
    }
}

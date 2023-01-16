package com.example.topmovies.models.mapper

import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.models.response.MovieDetailsResponse

class MovieDetailsResponseMapper : Mapper<MovieDetailsResponse, MovieDetails> {

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

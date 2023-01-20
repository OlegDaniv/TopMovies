package com.example.topmovies.models.mapper

import com.example.domain.models.Movie
import com.example.topmovies.models.entity.MovieEntity

object MovieEntityMapper : Mapper<MovieEntity, Movie> {

    override fun toModel(value: MovieEntity) = with(value) {
        Movie(
            id, rank, rankUpDown, title, fullTitle, year, imageUrl,
            crew, imDbRating, imDbRatingCount, isFavorite
        )
    }

    override fun fromModel(value: Movie) = with(value) {
        MovieEntity(
            id, rank, rankUpDown, title, fullTitle, year, imageUrl,
            crew, imDbRating, imDbRatingCount, isFavorite
        )
    }
}

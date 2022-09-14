package com.example.topmovies.repository

import com.example.topmovies.retrofit.MoviesApi

class MovieRepository constructor(private val movieApi: MoviesApi) {

    fun getMovies() = movieApi.getMovies()
    fun getMovieDetails(movieId: String) = movieApi.getMovieDetails(movieId)
}

package com.example.topmovies.repository

import com.example.topmovies.retrofit.MoviesApi

class MovieRepository constructor(private val movieApi: MoviesApi) {

    fun getMovies(apikey: String) = movieApi.getMovies(apikey)
    fun getMovieDetails(movieId: String, apikey: String) = movieApi.getMovieDetails(movieId, apikey)
}

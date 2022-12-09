package com.example.topmovies.retrofit

import com.example.topmovies.model.MovieDetails
import com.example.topmovies.model.MovieObject
import com.example.topmovies.unit.MOVIE_DETAILED
import com.example.topmovies.unit.TOP_100_MOVIES
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {
    @GET("$TOP_100_MOVIES/{apiKey}")
    fun getMovies(@Path("apiKey") apiKey: String): Call<MovieObject>

    @GET("$MOVIE_DETAILED/{apiKey}/{movieId}")
    fun getMovieDetails(@Path("apiKey") apiKey: String, @Path("movieId") movieId: String):
            Call<MovieDetails>
}

package com.example.topmovies.retrofit

import com.example.topmovies.models.MovieDetailsApi
import com.example.topmovies.models.MovieObject
import com.example.topmovies.unit.MOVIE_DETAILED
import com.example.topmovies.unit.TOP_100_MOVIES
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {
    
    @GET("$TOP_100_MOVIES/{apiKey}")
    fun getMovies(@Path("apiKey") apiKey: String): Call<MovieObject>
    
    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/movies")
    fun getMovies(): Call<MovieObject>
    
    @GET("$MOVIE_DETAILED/{apiKey}/{movieId}")
    fun getMovieDetails(@Path("apiKey") apiKey: String, @Path("movieId") movieId: String):
            Call<MovieDetailsApi>
    
    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/details")
    fun getMovieDetails(): Call<MovieDetailsApi>
}

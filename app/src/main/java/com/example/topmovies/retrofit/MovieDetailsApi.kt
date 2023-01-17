package com.example.topmovies.retrofit

import com.example.topmovies.models.response.MovieDetailsResponse
import com.example.topmovies.unit.MOVIE_DETAILED
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsApi {

    @GET("$MOVIE_DETAILED/{apiKey}/{movieId}")
    fun getMovieDetails(@Path("apiKey") apiKey: String, @Path("movieId") movieId: String):
            Call<MovieDetailsResponse>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/details")
    fun getMovieDetails(): Call<MovieDetailsResponse>
}

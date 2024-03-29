package com.example.topmovies.data.network.requests

import com.example.topmovies.data.network.models.MovieDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsApi {

    @GET("$MOVIE_DETAILS_END_POINT/{apiKey}/{movieId}")
    fun getMovieDetails(@Path("apiKey") apiKey: String, @Path("movieId") movieId: String):
            Call<MovieDetailsResponse>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/details")
    fun getMovieDetails(): Call<MovieDetailsResponse>

    companion object {
        private const val MOVIE_DETAILS_END_POINT = "/en/API/Title"
    }
}

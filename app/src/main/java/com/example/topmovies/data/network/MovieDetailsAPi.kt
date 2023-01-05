package com.example.topmovies.data.network

import com.example.topmovies.data.models.response.MovieDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsAPi {

    companion object {
        const val MOVIE_DETAILED = "/en/API/Title"
    }

    @GET("${MOVIE_DETAILED}/{apiKey}/{movieId}")
    fun getMovieDetails(@Path("apiKey") apiKey: String, @Path("movieId") movieId: String):
            Call<MovieDetailsResponse>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/details")
    fun getMovieDetails(): Call<MovieDetailsResponse>
}

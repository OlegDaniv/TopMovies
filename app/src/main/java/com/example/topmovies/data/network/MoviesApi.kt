package com.example.topmovies.data.network

import com.example.topmovies.data.models.response.MovieObjectResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {

    @GET("$MOVIES_END_POINT/{apiKey}")
    fun getMovies(@Path("apiKey") apiKey: String): Call<MovieObjectResponse>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/movies")
    fun getMovies(): Call<MovieObjectResponse>

    companion object {
        const val MOVIES_END_POINT = "/en/API/MostPopularMovies"

    }
}

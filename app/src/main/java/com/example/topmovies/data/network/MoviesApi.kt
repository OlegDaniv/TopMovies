package com.example.topmovies.data.network

import com.example.topmovies.data.models.response.MovieObjectResponse
import com.example.topmovies.data.repository.MoviesRepository.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {

    @GET("$TOP_100_MOVIES/{apiKey}")
    fun getMovies(@Path("apiKey") apiKey: String): Call<MovieObjectResponse>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/movies")
    fun getMovies(): Call<MovieObjectResponse>

    companion object {
      private  const val TOP_100_MOVIES = "/en/API/MostPopularMovies"
    }
}

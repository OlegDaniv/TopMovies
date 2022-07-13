package com.example.topmovies.retrofit

import com.example.topmovies.model.MovieObject
import com.example.topmovies.unit.BASE_URL
import com.example.topmovies.unit.TOP_100_MOVIES
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiServer {
    @GET(TOP_100_MOVIES)
    fun getAllMovies(): Call<MovieObject>

    companion object {
        var ApiService: ApiServer? = null

        fun getInstance(): ApiServer {
            if (ApiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                ApiService = retrofit.create(ApiServer::class.java)
            }
            return ApiService!!
        }
    }
}

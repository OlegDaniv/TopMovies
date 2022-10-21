package com.example.topmovies.repository

import com.example.topmovies.model.MovieDetails
import com.example.topmovies.model.MovieObject
import com.example.topmovies.retrofit.MoviesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository constructor(private val movieApi: MoviesApi) {
    
    fun getNewMovies(
        apikey: String, onSuccess: (MovieObject) -> Unit, onError: (String) -> Unit
    ) {
        movieApi.getMovies().enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.errorMessage.isNotEmpty()) onError(it.errorMessage)
                        else onSuccess(it)
                    }
                } else onError(response.code().toString())
            }
    
            override fun onFailure(call: Call<MovieObject>, t: Throwable) {
                onError(t.message.orEmpty())
            }
        })
    }
    
    fun getMovieDetails(
        movieId: String,
        apikey: String,
        onSuccess: (MovieDetails) -> Unit,
        onError: (String) -> Unit
    ) {
        movieApi.getMovieDetails()
            .enqueue(object : Callback<MovieDetails> {
                override fun onResponse(
                    call: Call<MovieDetails>,
                    response: Response<MovieDetails>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.errorMessage != null) onError(it.errorMessage)
                            else onSuccess(it)
                        }
                    } else {
                        onError(response.code().toString())
                    }
                }
                
                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    onError(t.message.orEmpty())
                }
            })
    }
}

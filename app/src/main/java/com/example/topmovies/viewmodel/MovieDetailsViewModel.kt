package com.example.topmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.MovieDetails
import com.example.topmovies.repository.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    fun getMovieDetails(movieId: String) {
        repository.getMovieDetails(movieId).enqueue(object :
            Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                _movieDetails.postValue(response.body())
            }

            override fun onFailure(call: Call<MovieDetails>, throwable: Throwable) {
                Log.e("getAllMovie onFailure", " ${throwable.message}")
            }
        })
    }
}

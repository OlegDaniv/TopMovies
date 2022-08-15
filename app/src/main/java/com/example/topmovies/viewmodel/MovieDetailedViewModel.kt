package com.example.topmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.MovieDetailed
import com.example.topmovies.repository.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailedViewModel constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movieDetailed = MutableLiveData<MovieDetailed>()
    val movieDetailed: LiveData<MovieDetailed> = _movieDetailed

    fun getMovieDetailed(movieId: String) {
        repository.getMovieDetailed(movieId).enqueue(object :
            Callback<MovieDetailed> {
            override fun onResponse(call: Call<MovieDetailed>, response: Response<MovieDetailed>) {
                _movieDetailed.postValue(response.body())
            }

            override fun onFailure(call: Call<MovieDetailed>, t: Throwable) {
                Log.e("getAllMovie onFailure", " ${t.message}")
            }
        })
    }
}
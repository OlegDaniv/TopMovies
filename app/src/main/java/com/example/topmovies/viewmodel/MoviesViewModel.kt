package com.example.topmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.Movie
import com.example.topmovies.model.MovieObject
import com.example.topmovies.repository.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        getAllMovies()
    }

    fun getAllMovies() {
        repository.getMovies().enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                _movies.postValue(response.body()?.items)
            }

            override fun onFailure(call: Call<MovieObject>, throwable: Throwable) {
                Log.e("getAllMovie onFailure", " ${throwable.message}")
            }
        })
    }
}


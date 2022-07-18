package com.example.topmovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.Movie
import com.example.topmovies.model.MovieObject
import com.example.topmovies.repository.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel constructor(private val repository: MovieRepository) : ViewModel() {
    val movieList = MutableLiveData<List<Movie>>()
    val errorMessage = MutableLiveData<String>()
    fun getAllMovies() {
        val response = repository.getAllMovies()
        response.enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                movieList.postValue(response.body()?.items)
            }

            override fun onFailure(call: Call<MovieObject>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}

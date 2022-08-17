package com.example.topmovies.viewmodel

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.topmovies.repository.MovieRepository

@Suppress("UNCHECKED_CAST")
class MovieModelFactory constructor(private val repository: MovieRepository) :
    ViewModelProvider.Factory {

    @NonNull
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            MoviesViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            MovieDetailsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

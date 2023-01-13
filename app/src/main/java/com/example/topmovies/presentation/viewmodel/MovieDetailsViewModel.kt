package com.example.topmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.usecase.GetMovieDetailsUseCase
import com.example.topmovies.domain.utils.Result.Failure
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.MovieDetails

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    fun resolveMovieDetails(id: String) {
        getMovieDetailsUseCase(id) {
            when (it) {
                is Failure -> handleError(it.error)
                is Success -> handleMovieDetails(it.result)
            }
        }
    }

    private fun handleMovieDetails(movieDetails: MovieDetails) {
        _movieDetails.value = movieDetails
    }
}

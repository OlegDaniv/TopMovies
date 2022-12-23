package com.example.topmovies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.datalayer.repository.MovieDetailsRepository.MovieDetails
import com.example.topmovies.domain.GetMovieDetailsUseCase

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    fun resolveMovieDetails(id: String) {
        getMovieDetailsUseCase(id) { resultOf ->
            resultOf.fold(
                { handledErrors(it)},
                { handleMovieDetails(it) }
            )
        }
    }

    private fun handleMovieDetails(movieDetails: MovieDetails) {
        _movieDetails.value = movieDetails
    }
}

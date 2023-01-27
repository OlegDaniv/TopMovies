package com.example.topmovies.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MovieDetails
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.utils.AppDispatchers
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val appDispatchers: AppDispatchers
) : BaseViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    fun resolveMovieDetails(movieId: String) = viewModelScope.launch(appDispatchers.main) {
        when (val result = getMovieDetailsUseCase(movieId)) {
            is Success -> handleMovieDetails(result.data)
            is Failure -> handleError(result.error)
        }
    }

    private fun handleMovieDetails(data: MovieDetails) {
        _movieDetails.value = data
    }
}

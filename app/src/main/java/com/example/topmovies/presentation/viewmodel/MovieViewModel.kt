package com.example.topmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.usecase.GetPairMoviesUseCase
import com.example.topmovies.domain.usecase.LoadNewMoviesUseCase
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Error
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.Movie
import com.example.topmovies.presentation.utils.EnumScreen
import com.example.topmovies.presentation.utils.EnumScreen.FAVORITE
import com.example.topmovies.presentation.utils.EnumScreen.MOVIES

class MovieViewModel constructor(
    private val getPairMovies: GetPairMoviesUseCase,
    private val loadNewMovies: LoadNewMoviesUseCase,
    private val updateFavoriteMovie: UpdateFavoriteMovieUseCase
) : BaseViewModel() {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()

    fun getObservableList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            MOVIES -> movies
            FAVORITE -> favoriteMovies
        }
    }

    fun getMovies() {
        getPairMovies(Unit) {
            handleResultOf(it)
        }
    }

    fun loadNewMovies() {
        loadNewMovies(Unit) { result ->
            handleMovies(result.asSuccess().result)
            handledErrors(result.asErrors().error)
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            MOVIES -> shiftMovieToFavorites(id, !favorite)
            FAVORITE -> shiftMovieToFavorites(id, false)
        }
    }

    private fun shiftMovieToFavorites(id: String, isFavorite: Boolean) {
        updateFavoriteMovie(Params(id, isFavorite)) {
            handleResultOf(it)
        }
    }

    private fun handleResultOf(result: Result<Failure, Pair<List<Movie>, List<Movie>>>) {
        when (result) {
            is Error -> handledErrors(result.error)
            is Success -> {
                handleMovies(result.result.first)
                handleFavoriteMovies(result.result.second)
            }
        }
    }

    private fun handleFavoriteMovies(newFavoriteMovies: List<Movie>) {
        favoriteMovies.value = newFavoriteMovies
    }

    private fun handleMovies(newMovie: List<Movie>) {
        movies.value = newMovie
    }
}

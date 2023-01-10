package com.example.topmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.usecase.*
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.presentation.models.Movie
import com.example.topmovies.presentation.utils.EnumScreen

class MovieViewModel constructor(
    private val getMovies: GetMoviesUseCase,
    private val getPairMovies: GetPairMoviesUseCase,
    private val getFavoriteMovie: GetFavoriteMovieUseCase,
    private val loadNewMovies: LoadNewMoviesUseCase,
    private val updateFavoriteMovie: UpdateFavoriteMovieUseCase
) : BaseViewModel() {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()

    fun getObservableList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            EnumScreen.MOVIES -> movies
            EnumScreen.FAVORITE -> favoriteMovies
        }
    }

    fun getMovies() {
        getPairMovies(Unit) { result ->
            val pair = result.getSuccess()
            pair?.first?.let { movies -> handleMovies(movies) }
            pair?.second?.let { favoriteMovies -> handleFavoriteMovies(favoriteMovies) }
        }
    }

    fun loadNewMovies() {
        loadNewMovies(Unit) { resultOfMovies ->
            resultOfMovies.fold(
                onFailed = { handledErrors(it) },
                onSuccess = { handleMovies(it) }
            )
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            EnumScreen.MOVIES ->
                shiftMovieToFavorites(id, !favorite)
            EnumScreen.FAVORITE ->
                shiftMovieToFavorites(id, false)
        }
    }

    private fun shiftMovieToFavorites(id: String, isFavorite: Boolean) {
        updateFavoriteMovie(Params(id, isFavorite)) {
            val pair = it.getSuccess()
            pair?.first?.let { it1 -> handleMovies(it1) }
            pair?.second?.let { it1 -> handleFavoriteMovies(it1) }
        }
    }

    private fun handleFavoriteMovies(newFavoriteMovies: List<Movie>) {
        favoriteMovies.value = newFavoriteMovies
    }

    private fun handleMovies(newMovie: List<Movie>) {
        movies.value = newMovie
    }
}

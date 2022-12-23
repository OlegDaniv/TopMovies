package com.example.topmovies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.datalayer.repository.MoviesRepository.Movie
import com.example.topmovies.domain.GetFavoriteMovieUseCase
import com.example.topmovies.domain.GetMoviesUseCase
import com.example.topmovies.domain.LoadNewMoviesUseCase
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.exeption.Failure
import com.example.topmovies.ui.viewmodel.MovieViewModel.EnumResult.Single
import com.example.topmovies.ui.viewmodel.MovieViewModel.EnumResult.WithFavoriteMovies
import com.example.topmovies.utils.EnumScreen
import com.example.topmovies.utils.ResultOf

class MovieViewModel constructor(
    private val getMovies: GetMoviesUseCase,
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
        getMovies(None()) { foldNewResult(it, WithFavoriteMovies) }
    }

    private fun handleFavoriteMovies(newFavoriteMovies: List<Movie>) {
        favoriteMovies.value = newFavoriteMovies
    }

    private fun handleMovies(newMovie: List<Movie>) {
        movies.value = newMovie
    }

    fun loadNewMovies() {
        loadNewMovies(None()) {
            foldNewResult(it, Single)
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
            foldNewResult(it, WithFavoriteMovies)
        }
    }

    private fun foldNewResult(
        resultOfMovies: ResultOf<Failure, List<Movie>>,
        enumResult: EnumResult
    ) {
        when (enumResult) {
            Single -> resultOfMovies.fold(::handledErrors, ::handleMovies)
            WithFavoriteMovies -> resultOfMovies.fold(
                { handledErrors(it) },
                {
                    handleMovies(it)
                    getFavoriteMovie(None()) { resultOfFavoriteMovie ->
                        resultOfFavoriteMovie.fold(::handledErrors, ::handleFavoriteMovies)
                    }
                })
        }
    }

    enum class EnumResult {
        Single, WithFavoriteMovies
    }
}

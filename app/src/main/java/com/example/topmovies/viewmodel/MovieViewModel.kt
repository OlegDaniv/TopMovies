package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.EnumScreen

class MovieViewModel constructor(
    private val repository: MovieRepository,
    sharedPref: SharedPreferences,
) : BaseViewModel(sharedPref) {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()
    private var _errorMessage: String? = null
    val errorMessage = MutableLiveData(_errorMessage)
    private val handler = Handler(Looper.getMainLooper())

    fun getMoviesList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            EnumScreen.MOVIES -> movies
            EnumScreen.FAVORITE -> favoriteMovies
        }
    }

    fun loadMovies() {
        repository.loadMovies { movieEntities ->
            movieEntities.takeIf { it.isNotEmpty() }?.let {
                handler.post { movies.value = it.map { it.toMovie() } }
                repository.loadFavoriteMovie {
                    handler.post { favoriteMovies.value = it.map { it.toMovie() } }
                }
            } ?: resolveMovies()
        }
    }

    fun resolveMovies() {
        repository.loadNewMovies(
            getApiKey(),
            onSuccess = {
                repository.upsertMovies(it.items.map { movieApi -> movieApi.toMovie() }) {
                    handler.post { movies.value = it.map { movie -> movie.toMovie() } }
                }
            },
            onError = { handler.post { errorMessage.postValue(it) } }
        )
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            EnumScreen.MOVIES ->
                if (favorite) removeMovieFromFavorites(id)
                else addMovieToFavorites(id)
            EnumScreen.FAVORITE ->
                removeMovieFromFavorites(id)
        }
    }

    private fun addMovieToFavorites(id: String) {
        repository.updateMovie(id, true)
        val mutableMovie = movies.value?.toMutableList()
        val movie = mutableMovie?.find { it.id == id }
        val index = mutableMovie?.indexOf(movie)
        val copy = movie!!.copy(isFavorite = true)
        mutableMovie.remove(movie)
        index?.let { mutableMovie.add(it, copy) }
        mutableMovie.let { movies.value = it }
    }

    private fun removeMovieFromFavorites(id: String) {
        repository.updateMovie(id, false)
        val mutableMovies = movies.value?.toMutableList()
        val movie = mutableMovies?.find { it.id == id }
        val copy = movie!!.copy(isFavorite = false)
        val index = mutableMovies.indexOf(movie)
        mutableMovies.remove(movie)
        mutableMovies.add(index, copy)
        mutableMovies.let { movies.value = it }
        val mutableFavoriteMovie = favoriteMovies.value?.toMutableList()
        mutableFavoriteMovie?.remove(movie)
        mutableFavoriteMovie?.let { favoriteMovies.value = it }
    }
}

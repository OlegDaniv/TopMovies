package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.model.Movie
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.EnumScreen

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val favoritePref: SharedPreferences,
    sharedPref: SharedPreferences
) : BaseViewModel(sharedPref) {

    private val movies = MutableLiveData<List<Movie>>()
    private val _favoriteMovies = mutableListOf<Movie>()
    private val favoriteMovies: MutableLiveData<List<Movie>> = MutableLiveData(_favoriteMovies)
    private var _errorMessage: String? = null
    val errorMessage = MutableLiveData(_errorMessage)

    fun getMoviesList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            EnumScreen.MOVIES -> movies
            EnumScreen.FAVORITE -> favoriteMovies
        }
    }

    fun getMovies() {
        movies.value ?: resolveMovies()
    }
    fun resolveMovies() {
        _favoriteMovies.clear()
        repository.getNewMovies(
            getApiKey(),
            onSuccess = {
                adjustFavoriteMovies(it.items)
                movies.postValue(it.items)
            },
            onError = { errorMessage.postValue(it) }
        )
    }

    private fun <T> LiveData<T>.update() {
        (this as? MutableLiveData<T>)?.let {
            value = value
        }
    }

    private fun adjustFavoriteMovies(movies: List<Movie>) {
        favoritePref.all.keys.toList().forEach { id ->
            movies.find { it.id == id }?.let { chosenMovie ->
                chosenMovie.isFavorite = true
                _favoriteMovies.add(chosenMovie)
            }
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            EnumScreen.MOVIES ->
                if (favorite) removeMovieFromFavorites(id)
                else addMovieToFavorites(id)
            EnumScreen.FAVORITE ->
                removeMovieFromFavorites(id)
        }
        favoriteMovies.update()
    }

    private fun addMovieToFavorites(id: String) {
        val mutableMovies = movies.value!!.toMutableList()
        val movie = movies.value!!.find { movie -> movie.id.contains(id) }
        val copy = movie?.copy(isFavorite = true)
        if (copy != null) {
            mutableMovies[movies.value!!.indexOf(movie)] = copy
            movies.postValue(mutableMovies)
            _favoriteMovies.add(copy)
            favoritePref.edit().putString(copy.id, "").apply()
        }
    }

    private fun removeMovieFromFavorites(id: String) {
        val mutableMovies = movies.value!!.toMutableList()
        val movie = movies.value!!.find { movie -> movie.id.contains(id) }
        val copy = movie?.copy(isFavorite = false)
        if (copy != null) {
            mutableMovies[movies.value!!.indexOf(movie)] = copy
            movies.postValue(mutableMovies)
            _favoriteMovies.remove(movie)
            favoritePref.edit().remove(copy.id).apply()
        }
    }
}

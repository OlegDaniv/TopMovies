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

    private fun <T> LiveData<T>.update() {
        (this as? MutableLiveData<T>)?.let {
            value = value
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
    
    private fun adjustFavoriteMovies(movies: List<Movie>) {
        favoritePref.all.keys.toList().forEach { id ->
            movies.find { it.id == id }?.let { chosenMovie ->
                chosenMovie.isFavorite = true
                _favoriteMovies.add(chosenMovie)
            }
        }
    }

    fun addFavoriteMovie(movie: Movie, screen: EnumScreen) {
        when (screen) {
            EnumScreen.MOVIES -> {
                if (movie.isFavorite) removeMovieFromFavorites(movie)
                else addMovieToFavorites(movie)
            }
            EnumScreen.FAVORITE -> removeMovieFromFavorites(movie)
        }
        favoriteMovies.update()
    }
    
    private fun addMovieToFavorites(movie: Movie) {
        val mutableMovies = movies.value!!.toMutableList()
        val copy = movie.copy(isFavorite = true)
        mutableMovies[movies.value!!.indexOf(movie)] = copy
        movies.postValue(mutableMovies)
        _favoriteMovies.add(copy)
        favoritePref.edit().putString(movie.id, "").apply()
    }

    private fun removeMovieFromFavorites(movie: Movie) {
        val mutableMovies = movies.value!!.toMutableList()
        val copy = movie.copy(isFavorite = false)
        mutableMovies[movies.value!!.indexOf(movie)] = copy
        movies.postValue(mutableMovies)
        _favoriteMovies.remove(movie)
        favoritePref.edit().remove(movie.id).apply()
    }
}

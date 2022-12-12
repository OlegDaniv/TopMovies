package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.model.Movie
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

    fun getMovies() {
        repository.getMovies { it ->
            it.takeIf { it.isNotEmpty() }.apply {
                handler.post { movies.value = it.map { it.toMovie() } }
            } ?: resolveMovies()
        }

        repository.getFavoriteMovie {
            handler.post { favoriteMovies.value = it.map { it.toMovie() } }
        }
    }


    fun resolveMovies() {
        repository.getNewMovies(
            getApiKey(),
            onSuccess = {
                repository.upsertMovies(it.items)
                repository.getMovies { movieEntities ->
                    handler.post {
                        movies.value = movieEntities.map { movieEntity -> movieEntity.toMovie() }
                    }
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
        repository.getMovies {
            handler.post { movies.value = it.map { it.toMovie() } }
        }
        repository.getFavoriteMovie {
            handler.post { favoriteMovies.value = it.map { it.toMovie() } }
        }
    }

    private fun removeMovieFromFavorites(id: String) {
        repository.updateMovie(id, false)
        repository.getMovies {
            handler.post { movies.value = it.map { it.toMovie() } }
        }
        repository.getFavoriteMovie {
            handler.post { favoriteMovies.value = it.map { it.toMovie() } }
        }
    }
}

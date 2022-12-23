package com.example.topmovies.datalayer.network

import com.example.topmovies.datalayer.repository.MoviesRepository.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {

    companion object {
        const val TOP_100_MOVIES = "/en/API/MostPopularMovies"
    }

    @GET("$TOP_100_MOVIES/{apiKey}")
    fun getMovies(@Path("apiKey") apiKey: String): Call<MovieObject>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/movies")
    fun getMovies(): Call<MovieObject>

    data class MovieObject(
        val items: List<MovieApi>,
        val errorMessage: String
    ) {
        companion object {
            val empty = MovieObject(emptyList(), "")
        }
    }

    data class MovieApi(
        val id: String,
        val rank: String,
        val rankUpDown: String,
        val title: String,
        val fullTitle: String,
        val year: String,
        @SerializedName("image")
        val imageUrl: String,
        val crew: String,
        val imDbRating: String,
        val imDbRatingCount: String,
        var isFavorite: Boolean = false
    ) {

        fun toMovie(): Movie {
            return Movie(
                id, rank, rankUpDown, title, fullTitle, year, imageUrl,
                crew, imDbRating, imDbRatingCount, isFavorite
            )
        }
    }
}

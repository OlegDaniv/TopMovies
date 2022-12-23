package com.example.topmovies.datalayer.network

import com.example.topmovies.datalayer.repository.MovieDetailsRepository
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsAPi {

    companion object {
        const val MOVIE_DETAILED = "/en/API/Title"
    }

    @GET("${MOVIE_DETAILED}/{apiKey}/{movieId}")
    fun getMovieDetails(@Path("apiKey") apiKey: String, @Path("movieId") movieId: String):
            Call<MovieDetailsBody>

    /** This method is for test, because 100 responses are not enough **/
    @GET("https://86e6737b-9ab8-444a-9c53-bdfa86309d8f.mock.pstmn.io/details")
    fun getMovieDetails(): Call<MovieDetailsBody>

    data class MovieDetailsBody(
        val id: String,
        val title: String,
        val year: String,
        val plot: String,
        @SerializedName("image")
        val imageUrl: String,
        val releaseDate: String,
        val runtimeStr: String,
        val genres: String,
        val imDbRating: String,
        val errorMessage: String? = null
    ) {

        companion object {
            val empty = MovieDetailsBody(
                "", "", "", "", "", "", "",
                "", "", null
            )
        }

        fun toMovieDetails(): MovieDetailsRepository.MovieDetails {
            return MovieDetailsRepository.MovieDetails(
                id, title, year, plot, imageUrl, releaseDate,
                runtimeStr, genres, imDbRating, errorMessage
            )
        }
    }
}

package com.example.topmovies.datalayer.repository

import com.example.topmovies.datalayer.dao.MovieDetailsDao
import com.example.topmovies.datalayer.dao.MovieDetailsDao.MovieDetailsEntity
import com.example.topmovies.datalayer.network.MovieDetailsRequest
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf
import com.example.topmovies.utils.ResultOf.Failed
import com.example.topmovies.utils.ResultOf.Success
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface MovieDetailsRepository {

    fun getMovieDetails(id: String): ResultOf<Failure, MovieDetails>

    fun insertMovieDetails(
        movie: MovieDetailsEntity,
        executor: ExecutorService = Executors.newSingleThreadExecutor()
    )

    fun loadNewMovieDetails(id: String): ResultOf<Failure, MovieDetails>

    class MovieDetailsRepositoryImp(
        private val movieDetailsDao: MovieDetailsDao,
        private val movieDetailsRequest: MovieDetailsRequest
    ) : MovieDetailsRepository {

        override fun getMovieDetails(id: String): ResultOf<Failure, MovieDetails> {
            val movieDetails = movieDetailsDao.getMovieDetailsEntityById(id)
            return if (movieDetails == null) {
                val newMovieDetails = loadNewMovieDetails(id)
                newMovieDetails.fold(
                    onFailed = { Failed(it) },
                    onSuccess = { movieDetailsDao.insertMovieDetailsEntity(it.toMovieDetailsEntity()) }
                )
                newMovieDetails
            } else {
                Success(movieDetails.toMovieDetails())
            }
        }

        override fun insertMovieDetails(
            movie: MovieDetailsEntity,
            executor: ExecutorService
        ) {
            executor.execute { movieDetailsDao.insertMovieDetailsEntity(movie) }
        }

        override fun loadNewMovieDetails(id: String): ResultOf<Failure, MovieDetails> {
            return movieDetailsRequest.loadMovieDetails(id)
        }
    }

    data class MovieDetails(
        val id: String,
        val title: String,
        val year: String,
        val plot: String,
        val imageUrl: String,
        val releaseDate: String,
        val runtimeStr: String,
        val genres: String,
        val imDbRating: String,
        val errorMessage: String? = null
    ) {

        fun toMovieDetailsEntity(): MovieDetailsEntity {
            return MovieDetailsEntity(
                id, title, year, plot, imageUrl, releaseDate,
                runtimeStr, genres, imDbRating, errorMessage
            )
        }
    }
}

package com.example.topmovies.repository

import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.domain.UseCase.Result
import com.example.topmovies.models.MovieDetails
import com.example.topmovies.models.MovieDetailsEntity
import com.example.topmovies.retrofit.MoviesApi

class MovieDetailsRepositoryImpl(
    private val api: MoviesApi,
    private val movieDetailsDao: MovieDetailsDao,
) : MovieDetailsRepository {

    override fun getMovieDetails(id: String) = movieDetailsDao.getMovieDetails(id)

    override fun insertMovieDetails(entity: MovieDetailsEntity) {
        movieDetailsDao.insert(entity)
    }

    override fun loadNewMovieDetails(id: String): Result<MovieDetails> {
        return try {
            val response = api.getMovieDetails().execute()
            if (response.isSuccessful) {
                response.body()?.let { Result(it.toMovieDetails()) }
                    ?: Result(MovieDetails.empty, "Body is empty")
            } else {
                Result(MovieDetails.empty, response.code().toString())
            }
        } catch (e: Exception) {
            Result(MovieDetails.empty, e.message.toString())
        }
    }


}
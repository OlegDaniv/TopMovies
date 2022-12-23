package com.example.topmovies.datalayer.dao

import androidx.room.*
import com.example.topmovies.datalayer.repository.MovieDetailsRepository

@Dao
abstract class MovieDetailsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovieDetailsEntity(entity: MovieDetailsEntity)

    @Query("SELECT * FROM tb_movie_details WHERE id = :id")
    abstract fun getMovieDetailsEntityById(id: String): MovieDetailsEntity?

    @Entity(tableName = "tb_movie_details")
    data class MovieDetailsEntity(
        @PrimaryKey val id: String,
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

        fun toMovieDetails(): MovieDetailsRepository.MovieDetails {
            return MovieDetailsRepository.MovieDetails(
                id, title, year, plot, imageUrl, releaseDate,
                runtimeStr, genres, imDbRating, errorMessage
            )
        }
    }
}

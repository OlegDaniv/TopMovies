package com.example.topmovies.data.dao

import androidx.room.*
import com.example.topmovies.data.models.entity.MovieDetailsEntity

@Dao
abstract class MovieDetailsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovieDetails(entity: MovieDetailsEntity)

    @Query("SELECT * FROM movie_details WHERE id = :id")
    abstract fun getMovieDetailsById(id: String): MovieDetailsEntity?
}
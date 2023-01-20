package com.example.topmovies.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.topmovies.data.database.models.MovieDetailsEntity

@Dao
abstract class MovieDetailsDao {

    @Query("Select * from tb_movie_details where id = :id")
    abstract fun getMovieDetails(id: String): MovieDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovieDetails(entity: MovieDetailsEntity)
}

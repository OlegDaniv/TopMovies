package com.example.topmovies.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.topmovies.model.MovieDetailsEntity

@Dao
abstract class MovieDetailsDao : BaseDao<MovieDetailsEntity> {

    @Query("Select * from tb_movie_details where id = :id")
    abstract fun getMovieDetailsById(id: String): MovieDetailsEntity?
}

package com.example.topmovies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.topmovies.data.dao.MovieDetailsDao
import com.example.topmovies.data.dao.MoviesDao
import com.example.topmovies.data.models.entity.MovieDetailsEntity
import com.example.topmovies.data.models.entity.MovieEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    abstract fun movieDetails(): MovieDetailsDao
}

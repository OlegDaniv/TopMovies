package com.example.topmovies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.models.MovieDetailsEntity
import com.example.topmovies.models.MovieEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    abstract fun movieDetails(): MovieDetailsDao
}

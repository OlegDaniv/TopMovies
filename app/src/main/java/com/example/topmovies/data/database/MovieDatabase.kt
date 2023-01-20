package com.example.topmovies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.topmovies.data.database.dao.MovieDetailsDao
import com.example.topmovies.data.database.dao.MoviesDao
import com.example.topmovies.data.database.models.MovieDetailsEntity
import com.example.topmovies.data.database.models.MovieEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    abstract fun movieDetails(): MovieDetailsDao
}

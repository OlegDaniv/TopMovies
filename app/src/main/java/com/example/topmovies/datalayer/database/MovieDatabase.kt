package com.example.topmovies.datalayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.topmovies.datalayer.dao.MovieDetailsDao
import com.example.topmovies.datalayer.dao.MovieDetailsDao.MovieDetailsEntity
import com.example.topmovies.datalayer.dao.MoviesDao
import com.example.topmovies.datalayer.dao.MoviesDao.MovieEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    abstract fun movieDetails(): MovieDetailsDao
}

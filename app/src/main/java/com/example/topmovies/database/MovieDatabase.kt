package com.example.topmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.di.DATABASE_NAME
import com.example.topmovies.model.MovieDetailsEntity
import com.example.topmovies.model.MovieEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    abstract fun movieDetails(): MovieDetailsDao

    companion object {
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, MovieDatabase::class.java, DATABASE_NAME)
                            .build()
                }
            }
            return INSTANCE
        }
    }

}

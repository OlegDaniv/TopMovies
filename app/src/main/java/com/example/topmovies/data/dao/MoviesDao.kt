package com.example.topmovies.data.dao

import androidx.room.*
import com.example.topmovies.data.models.entity.MovieEntity
import com.example.topmovies.data.repository.MoviesRepository.*
import com.example.topmovies.presentation.models.Movie

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovieEntity(entity: MovieEntity)

    @Query("SELECT * From movies")
    abstract fun getMoviesEntity(): List<MovieEntity>

    @Query("Select * from movies where id = :id")
    abstract fun getMovieEntityById(id: String): MovieEntity?

    @Query("select * from movies where isFavorite = :isFavorite ")
    abstract fun getFavoriteMoviesEntity(isFavorite: Boolean): List<MovieEntity>

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract fun updateMovieEntity(id: String, isFavorite: Boolean)

    @Query("UPDATE movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract fun updateMovieEntity(id: String, rank: String, rankUpDown: String)

    @Transaction
    open fun upsertMoviesEntity(movies: List<Movie>) {

        movies.forEach { movie ->
            getMovieEntityById(movie.id)
                ?.let {
                    updateMovieEntity(
                        movie.id,
                        movie.rank,
                        movie.rankUpDown
                    )
                }
                ?: insertMovieEntity(movie.toMovieEntity())
        }
    }
}

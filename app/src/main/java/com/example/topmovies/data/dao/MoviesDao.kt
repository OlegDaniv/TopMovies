package com.example.topmovies.data.dao

import androidx.room.*
import com.example.topmovies.data.models.entity.MovieEntity
import com.example.topmovies.data.repository.MoviesRepository.*
import com.example.topmovies.presentation.models.Movie

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovie(entity: MovieEntity)

    @Query("SELECT * From movies")
    abstract fun getMovies(): List<MovieEntity>

    @Query("Select * from movies where id = :id")
    abstract fun getMovieById(id: String): MovieEntity?

    @Query("select * from movies where isFavorite = :isFavorite ")
    abstract fun getFavoriteMovies(isFavorite: Boolean): List<MovieEntity>

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract fun updateMovie(id: String, isFavorite: Boolean)

    @Query("UPDATE movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract fun updateMovie(id: String, rank: String, rankUpDown: String)

    @Transaction
    open fun upsertMovies(movies: List<Movie>) {

        movies.forEach { movie ->
            getMovieById(movie.id)
                ?.let {
                    updateMovie(
                        movie.id,
                        movie.rank,
                        movie.rankUpDown
                    )
                }
                ?: insertMovie(movie.toMovieEntity())
        }
    }
}

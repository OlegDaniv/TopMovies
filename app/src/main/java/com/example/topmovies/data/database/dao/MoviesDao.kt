package com.example.topmovies.data.database.dao

import androidx.room.*
import com.example.topmovies.data.models.entites.MovieEntity

@Dao
abstract class MoviesDao {

    @Query("SELECT * From tb_movies")
    abstract fun getMovies(): List<MovieEntity>

    @Query("Select * from tb_movies where id = :id")
    abstract fun getMovie(id: String): MovieEntity?

    @Query("select * from tb_movies where isFavorite = 1")
    abstract fun getFavoriteMovies(): List<MovieEntity>

    @Query("UPDATE tb_movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract fun updateMovie(id: String, isFavorite: Boolean)

    @Query("UPDATE tb_movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract fun updateMovie(id: String, rank: String, rankUpDown: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovie(entity: MovieEntity)

    @Transaction
    open fun upsertMovies(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            getMovie(movie.id)?.let {
                updateMovie(
                    movie.id, movie.rank, movie.rankUpDown
                )
            } ?: insertMovie(movie)
        }
    }
}

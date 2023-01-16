package com.example.topmovies.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.topmovies.models.entity.MovieEntity

@Dao
abstract class MoviesDao : BaseDao<MovieEntity> {

    @Query("SELECT * From tb_movies")
    abstract fun getMovies(): List<MovieEntity>

    @Query("Select * from tb_movies where id = :id")
    abstract fun getMovie(id: String): MovieEntity?

    @Query("select * from tb_movies where isFavorite = :isFavorite ")
    abstract fun getFavoriteMovies(isFavorite: Boolean): List<MovieEntity>

    @Query("UPDATE tb_movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract fun updateMovie(id: String, isFavorite: Boolean)

    @Query("UPDATE tb_movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract fun updateMovie(id: String, rank: String, rankUpDown: String)

    @Transaction
    open fun upsertMovies(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            getMovieEntityById(movie.id)?.let {
                updateMovie(
                    movie.id,
                    movie.rank,
                    movie.rankUpDown
                )
            }
                ?: insert(movie)
        }
    }
}

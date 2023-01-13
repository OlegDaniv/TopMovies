package com.example.topmovies.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.domain.toMovieEntity
import com.example.topmovies.models.entity.MovieEntity

@Dao
abstract class MoviesDao : BaseDao<MovieEntity> {

    @Query("SELECT * From tb_movies")
    abstract fun getMoviesEntity(): List<MovieEntity>

    @Query("Select * from tb_movies where id = :id")
    abstract fun getMovieEntityById(id: String): MovieEntity?

    @Query("select * from tb_movies where isFavorite = :isFavorite ")
    abstract fun getFavoriteMoviesEntity(isFavorite: Boolean): List<MovieEntity>

    @Query("UPDATE tb_movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract fun updateMovieEntity(id: String, isFavorite: Boolean)

    @Query("UPDATE tb_movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract fun updateMovie(id: String, rank: String, rankUpDown: String)

    @Transaction
    open fun upsertMoviesEntity(movies: List<Movie>) {
        movies.forEach { movie ->
            getMovieEntityById(movie.id)?.let {
                updateMovie(
                    movie.id,
                    movie.rank,
                    movie.rankUpDown
                )
            }
                ?: insert(movie.toMovieEntity())
        }
    }
}

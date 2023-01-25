package com.example.topmovies.data.database.dao

import androidx.room.*
import com.example.topmovies.data.database.models.MovieEntity

@Dao
abstract class MoviesDao {

    @Query("SELECT * From tb_movies")
    abstract suspend fun getMovies(): List<MovieEntity>

    @Query("Select * from tb_movies where id = :id")
    abstract suspend fun getMovie(id: String): MovieEntity?

    @Query("select * from tb_movies where isFavorite = 1")
    abstract suspend fun getFavoriteMovies(): List<MovieEntity>

    @Query("UPDATE tb_movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract suspend fun updateMovie(id: String, isFavorite: Boolean)

    @Query("UPDATE tb_movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract suspend fun updateMovie(id: String, rank: String, rankUpDown: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMovie(entity: MovieEntity)

    @Transaction
    open suspend fun upsertMovies(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            getMovie(movie.id)?.let {
                updateMovie(
                    movie.id, movie.rank, movie.rankUpDown
                )
            } ?: insertMovie(movie)
        }
    }
}

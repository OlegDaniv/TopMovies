package com.example.topmovies.data.dao

import androidx.room.*
import com.example.topmovies.data.models.entity.MovieEntity
import com.example.topmovies.data.repository.MoviesRepository.*
import com.example.topmovies.domain.exeption.Failure
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.presentation.models.Movie

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovieEntity(entity: MovieEntity)

    @Query("SELECT * From tb_movies")
    abstract fun getMoviesEntity(): List<MovieEntity>

    @Query("Select * from tb_movies where id = :id")
    abstract fun getMovieEntityById(id: String): MovieEntity?

    @Query("select * from tb_movies where isFavorite = :isFavorite ")
    abstract fun getFavoriteMoviesEntity(isFavorite: Boolean): List<MovieEntity>

    @Query("UPDATE tb_movies SET isFavorite = :isFavorite WHERE id = :id")
    abstract fun updateMovieEntity(id: String, isFavorite: Boolean)

    @Query("UPDATE tb_movies SET rank = :rank,rankUpDown = :rankUpDown  WHERE id = :id")
    abstract fun updateMovieEntity(id: String, rank: String, rankUpDown: String)

    @Transaction
    open fun upsertMoviesEntity(movies: ResultOf<Failure, List<Movie>>) {
        movies.fold({}, {
            it.forEach { movie ->
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
        })
    }
}

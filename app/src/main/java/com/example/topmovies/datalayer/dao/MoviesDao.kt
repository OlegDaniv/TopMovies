package com.example.topmovies.datalayer.dao

import androidx.room.*
import com.example.topmovies.datalayer.repository.MoviesRepository.*
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf

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

    @Entity(tableName = "tb_movies")
    data class MovieEntity @JvmOverloads constructor(
        @PrimaryKey val id: String,
        val rank: String,
        val rankUpDown: String,
        val title: String,
        val fullTitle: String,
        val year: String,
        val imageUrl: String,
        val crew: String,
        val imDbRating: String,
        val imDbRatingCount: String,
        var isFavorite: Boolean = false
    ) {
        fun toMovie(): Movie {
            return Movie(
                id, rank, rankUpDown, title, fullTitle,
                year, imageUrl, crew, imDbRating, imDbRatingCount, isFavorite
            )
        }
    }
}

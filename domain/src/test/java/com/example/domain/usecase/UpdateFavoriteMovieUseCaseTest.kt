package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import java.util.concurrent.ExecutorService

internal class UpdateFavoriteMovieUseCaseTest {
    private lateinit var repository: MoviesRepository
    private lateinit var handler: HandlerWrapper
    private lateinit var executor: ExecutorService
    private lateinit var useCase: UpdateFavoriteMovieUseCase
    private lateinit var movie: Movie
    private lateinit var movies: List<Movie>
    private lateinit var favoriteMovies: List<Movie>

    @BeforeEach
    fun setUp() {
        movie = Movie(
            MOVIES_ID, "", "", "", "", "", "", "", "", "", false
        )
        repository = mock(MoviesRepository::class.java)
        handler = mock(HandlerWrapper::class.java)
        executor = mock(ExecutorService::class.java)
        useCase = UpdateFavoriteMovieUseCase(repository, executor, handler)
        movies = listOf(movie)
        favoriteMovies = listOf(movie)
    }

    @Test
    fun `should get data from repository`() {
        `when`(repository.getMovies()).thenReturn(Success(movies))
        `when`(repository.getFavoriteMovies()).thenReturn(favoriteMovies)

        useCase.execute(Params(MOVIES_ID, true))
        verify(repository).updateMovie(MOVIES_ID, true)
        verify(repository).getMovies()
        verify(repository).getFavoriteMovies()
    }

    @Test
    fun `should return success`() {
        `when`(repository.getMovies()).thenReturn(Success(movies))
        `when`(repository.getFavoriteMovies()).thenReturn(favoriteMovies)
        assertTrue(useCase.execute(Params(MOVIES_ID, true)) is Success)
    }

    @Test
    fun `should return error`() {
        `when`(repository.getMovies()).thenReturn(Failure(ServerError))
        `when`(repository.getFavoriteMovies()).thenReturn(favoriteMovies)
        assertTrue(useCase.execute(Params(MOVIES_ID, true)) is Failure)
    }

    companion object {
        const val MOVIES_ID = "123"
    }
}

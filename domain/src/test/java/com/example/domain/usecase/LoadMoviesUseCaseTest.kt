package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
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

internal class LoadMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var handler: HandlerWrapper
    private lateinit var executor: ExecutorService
    private lateinit var useCase: LoadMoviesUseCase
    private lateinit var movie: Movie
    private lateinit var movies: List<Movie>

    @BeforeEach
    fun setUp() {
        repository = mock(MoviesRepository::class.java)
        handler = mock(HandlerWrapper::class.java)
        executor = mock(ExecutorService::class.java)
        useCase = LoadMoviesUseCase(repository, executor, handler)
        movie = Movie(
            "", "", "", "", "", "", "", "", "", "", false
        )
        movies = listOf(movie)
    }

    @Test
    fun `should invoke correct method from repository`() {
        `when`(repository.loadNewMovies()).thenReturn(Success(movies))
        useCase.execute(Unit)
        verify(repository).loadNewMovies()
    }

    @Test
    fun `should return success`() {
        `when`(repository.loadNewMovies()).thenReturn(Success(movies))
        assertTrue(useCase.execute(Unit) is Success)
    }

    @Test
    fun `should return error`() {
        `when`(repository.loadNewMovies()).thenReturn(Failure(ServerError))
        assertTrue(useCase.execute(Unit) is Failure)
    }
}

package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import java.util.concurrent.ExecutorService

internal class LoadMoviesUseCaseTest {

    private val repository = mock(MoviesRepository::class.java)
    private val handler = mock(HandlerWrapper::class.java)
    private val executor = mock(ExecutorService::class.java)
    private val useCase = LoadMoviesUseCase(repository, executor, handler)
    private val mockedMovie = Movie(
        "", "", "", "", "", "",
        "", "", "", "", false
    )
    private val mockedMovies = listOf(mockedMovie)

    @Test
    fun `test execute return success`() {
        `when`(repository.loadNewMovies()).thenReturn(Success(mockedMovies))
        val result = useCase.execute(Unit)
        verify(repository).loadNewMovies()
        assertTrue(result is Success)
        assertEquals(mockedMovies, (result as Success).data)
        assertNotEquals(
            mockedMovie.copy(title = "New title"), result.data
        )
    }

    @Test
    fun `test execute return error`() {
        `when`(repository.loadNewMovies()).thenReturn(Failure(ServerError))
        val result = useCase.execute(Unit)
        assertTrue(result is Failure)
        assertEquals(ServerError, (result as Failure).error)
    }
}
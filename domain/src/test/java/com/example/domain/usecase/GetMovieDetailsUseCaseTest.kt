package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Success
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import java.util.concurrent.ExecutorService

internal class GetMovieDetailsUseCaseTest {

    private val mockedMovieDetails = MovieDetails(
        "123", "", "", "", "",
        "", "", "", "", ""
    )
    private val repository = mock(MovieDetailsRepository::class.java)
    private val handler = mock(HandlerWrapper::class.java)
    private val executor = mock(ExecutorService::class.java)
    private val useCase = GetMovieDetailsUseCase(repository, executor, handler)

    @Test
    fun `test execute return success`() {
        `when`(repository.getMovieDetails("123")).thenReturn(Success(mockedMovieDetails))
        val result = useCase.execute("123")
        verify(repository).getMovieDetails("123")
        assertTrue(result is Success)
        assertEquals(mockedMovieDetails, (result as Success).data)
        assertNotEquals(
            mockedMovieDetails.copy(title = "New title"), result.data
        )
    }

    @Test
    fun `test execute return error`() {
        `when`(repository.getMovieDetails("123")).thenReturn(Result.Failure(Error.ServerError))
        val result = useCase.execute("123")
        assertTrue(result is Result.Failure)
        assertEquals(Error.ServerError, (result as Result.Failure).error)
    }

    @Test
    fun `test execute returns movieDetails for another id`() {
        `when`(repository.getMovieDetails("")).thenReturn(Success(mockedMovieDetails))
        assertFalse(useCase.execute("123") is Success)
    }
}

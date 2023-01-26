package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.util.concurrent.ExecutorService

internal class GetMovieDetailsUseCaseTest {

    private lateinit var repository: MovieDetailsRepository
    private lateinit var handler: HandlerWrapper
    private lateinit var executor: ExecutorService
    private lateinit var useCase: GetMovieDetailsUseCase
    private lateinit var movieDetails: MovieDetails

    @BeforeEach
    fun setUp() {
        repository = mock(MovieDetailsRepository::class.java)
        handler = mock(HandlerWrapper::class.java)
        executor = mock(ExecutorService::class.java)
        useCase = GetMovieDetailsUseCase(repository, executor, handler)
        movieDetails = MovieDetails(
            MOVIE_ID, "", "", "", "", "", "", "", "", ""
        )
    }

    @Test
    fun `should invoke correct method from repository`() {
        `when`(repository.getMovieDetails(any())).thenReturn(Success(movieDetails))
        useCase.execute(MOVIE_ID)
        verify(repository).getMovieDetails(MOVIE_ID)
    }

    @Test
    fun `should return success`() {
        `when`(repository.getMovieDetails(any())).thenReturn(Success(movieDetails))
        assertTrue(useCase.execute(MOVIE_ID) is Success)
    }

    @Test
    fun `shouldn't return Error`() {
        `when`(repository.getMovieDetails(MOVIE_ID)).thenReturn(Failure(ServerError))
        assertTrue(useCase.execute(MOVIE_ID) is Failure)
    }

    companion object {
        private const val MOVIE_ID = "tt1630029"
    }
}

package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.concurrent.ExecutorService

internal class GetMoviesPairUseCaseTest {
    private val repository = mock(MoviesRepository::class.java)
    private val handler = mock(HandlerWrapper::class.java)
    private val executor = mock(ExecutorService::class.java)
    private val usecase = GetMoviesPairUseCase(repository, executor, handler)
    private val mockedMovie = Movie(
        "", "", "", "", "", "",
        "", "", "", "", false
    )

    @Test
    fun `test execute return success`() {
        val movies = listOf(mockedMovie)
        val favoriteMovies = listOf(mockedMovie)
        `when`(repository.getMovies()).thenReturn(Success(movies))
        `when`(repository.getFavoriteMovies()).thenReturn(favoriteMovies)
        val result = usecase.execute(Unit)
        assertTrue(result is Success)
        assertEquals(Pair(movies, favoriteMovies), (result as Success).data)
    }

    @Test
    fun `test execute return error`(){
        val favoriteMovies = listOf(mockedMovie)
        `when`(repository.getMovies()).thenReturn(Failure(ServerError))
        `when`(repository.getFavoriteMovies()).thenReturn(favoriteMovies)
        val result = usecase.execute(Unit)
        assertTrue(result is Failure)
        assertEquals(ServerError, (result as Failure).error)
    }
}
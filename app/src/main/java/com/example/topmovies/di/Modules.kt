package com.example.topmovies.di

import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.room.Room
import com.example.topmovies.database.MovieDatabase
import com.example.topmovies.domain.GetMovieDetailsUseCase
import com.example.topmovies.domain.GetMoviesUseCase
import com.example.topmovies.domain.LoadMoviesUseCase
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase
import com.example.topmovies.models.mapper.MovieDetailsEntityMapper
import com.example.topmovies.models.mapper.MovieEntityMapper
import com.example.topmovies.models.mapper.MovieDetailsResponseMapper
import com.example.topmovies.models.mapper.MovieResponseMapper
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.BASE_URL
import com.example.topmovies.viewmodel.MovieDetailsViewModel
import com.example.topmovies.viewmodel.MovieViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

const val DATABASE_NAME: String = "movie_database"

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }
    single {
        OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS).build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get()).build()
    }
    single { get<Retrofit>().create(MoviesApi::class.java) }
}

val viewModelModule = module {
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { MovieViewModel(get(), get(), get()) }
}

val useCaseModule = module {
    single { GetMoviesUseCase(get(), get(), get()) }
    single { UpdateFavoriteMovieUseCase(get(), get(), get()) }
    single { LoadMoviesUseCase(get(), get(), get()) }
    single { GetMovieDetailsUseCase(get(), get(), get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), MovieDatabase::class.java, DATABASE_NAME).build()
    }
    single { get<MovieDatabase>().movieDao() }
    single { get<MovieDatabase>().movieDetails() }
    single { MovieDetailsEntityMapper() }
    single { MovieEntityMapper() }
    single { MovieDetailsResponseMapper() }
    single { MovieResponseMapper() }
}

val repositoryModule = module {
    single { MovieRepository(get(), get(), get(), get(), get(), get(), get(), get()) }
}

val appModule = module {
    single { Handler(Looper.getMainLooper()) }
    single { Executors.newFixedThreadPool(4) }
    single { getDefaultSharedPreferences(androidApplication()) }
}

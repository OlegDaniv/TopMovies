package com.example.topmovies.di

import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.room.Room
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.repositores.MoviesRepository
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.usecase.GetMoviesPairUseCase
import com.example.domain.usecase.LoadMoviesUseCase
import com.example.domain.usecase.UpdateFavoriteMovieUseCase
import com.example.domain.utils.HandlerWrapper
import com.example.topmovies.database.MovieDatabase
import com.example.topmovies.repositores.MovieDetailsRepositoryImpl
import com.example.topmovies.repositores.MoviesRepositoryImpl
import com.example.topmovies.retrofit.MovieDetailsApi
import com.example.topmovies.retrofit.MovieDetailsRequest
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.retrofit.MoviesRequest
import com.example.topmovies.unit.BASE_URL
import com.example.topmovies.utils.HandlerWrapperImpl
import com.example.topmovies.utils.NetworkHandler
import com.example.topmovies.viewmodel.MovieDetailsViewModel
import com.example.topmovies.viewmodel.MovieViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
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
            .connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()
    }
    single {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(get()).build()
    }
    single(named("movieApi")) { get<Retrofit>().create(MoviesApi::class.java) }
    single(named("detailsApi")) { get<Retrofit>().create(MovieDetailsApi::class.java) }
    single { MoviesRequest(get(named("movieApi")), get()) }
    single { MovieDetailsRequest(get(named("detailsApi")), get()) }
}

val viewModelModule = module {
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { MovieViewModel(get(), get(), get()) }
}

val useCaseModule = module {
    single { GetMoviesPairUseCase(get(), get(), get()) }
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
}

val repositoryModule = module {
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }
    single<MovieDetailsRepository> { MovieDetailsRepositoryImpl(get(), get()) }
}

val appModule = module {
    single { Handler(Looper.getMainLooper()) }
    single { Executors.newFixedThreadPool(4) }
    single { getDefaultSharedPreferences(androidApplication()) }
    single { NetworkHandler(androidApplication()) }
    single<HandlerWrapper> { HandlerWrapperImpl(Handler(Looper.getMainLooper())) }
}

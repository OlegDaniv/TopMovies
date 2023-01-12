package com.example.topmovies.di

import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.room.Room
import com.example.topmovies.data.database.MovieDatabase
import com.example.topmovies.data.network.MovieDetailsApi
import com.example.topmovies.data.network.MovieDetailsRequest
import com.example.topmovies.data.network.MoviesApi
import com.example.topmovies.data.network.MoviesRequest
import com.example.topmovies.data.repository.MovieDetailsRepository
import com.example.topmovies.data.repository.MovieDetailsRepository.MovieDetailsRepositoryImpl
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.data.repository.MoviesRepository.MoviesRepositoryImpl
import com.example.topmovies.data.utils.NetworkHandler
import com.example.topmovies.domain.usecase.*
import com.example.topmovies.domain.utils.GetPreference
import com.example.topmovies.presentation.viewmodel.MovieDetailsViewModel
import com.example.topmovies.presentation.viewmodel.MovieViewModel
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

private const val DATABASE_NAME: String = "movie_database"
private const val BASE_URL: String = "https://imdb-api.com"

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
    single(named("moviesRetrofit")) { get<Retrofit>().create(MoviesApi::class.java) }
    single(named("detailsRetrofit")) { get<Retrofit>().create(MovieDetailsApi::class.java) }
    single { MoviesRequest(get(named("moviesRetrofit")), get(), get()) }
    single { MovieDetailsRequest(get(named("detailsRetrofit")), get(), get()) }
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

val useCaseModule = module {
    single { GetMovieDetailsUseCase(get(), get(), get()) }
    single { GetPreference(get()) }
    single { LoadNewMoviesUseCase(get(), get(), get()) }
    single { UpdateFavoriteMovieUseCase(get(), get(), get()) }
    single { GetMoviesPairUseCase(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { MovieViewModel(get(), get(), get()) }
}

val appModule = module {
    single { Handler(Looper.getMainLooper()) }
    single { Executors.newFixedThreadPool(4) }
    single { getDefaultSharedPreferences(androidApplication()) }
    single { NetworkHandler(androidApplication()) }
}

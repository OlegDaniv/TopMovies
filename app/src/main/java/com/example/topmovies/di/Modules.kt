package com.example.topmovies.di

import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.room.Room
import com.example.topmovies.data.database.MovieDatabase
import com.example.topmovies.data.network.MovieDetailsAPi
import com.example.topmovies.data.network.MovieDetailsRequest
import com.example.topmovies.data.network.MoviesApi
import com.example.topmovies.data.network.MoviesRequest
import com.example.topmovies.data.repository.MovieDetailsRepository
import com.example.topmovies.data.repository.MovieDetailsRepository.MovieDetailsRepositoryImp
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.data.repository.MoviesRepository.MoviesRepositoryImp
import com.example.topmovies.domain.usecase.*
import com.example.topmovies.presentation.viewmodel.MovieDetailsViewModel
import com.example.topmovies.presentation.viewmodel.MovieViewModel
import com.example.topmovies.data.utils.NetworkHandler
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
const val BASE_URL: String = "https://imdb-api.com"

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
    single(named("moviesR")) { get<Retrofit>().create(MoviesApi::class.java) }
    single(named("detailsR")) { get<Retrofit>().create(MovieDetailsAPi::class.java) }
    single { MoviesRequest(get(named("moviesR")), get(), get()) }
    single { MovieDetailsRequest(get(named("detailsR")), get(), get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), MovieDatabase::class.java, DATABASE_NAME).build()
    }
    single { get<MovieDatabase>().movieDao() }
    single { get<MovieDatabase>().movieDetails() }
}
val repositoryModule = module {
    single<MoviesRepository> { MoviesRepositoryImp(get(), get()) }
    single<MovieDetailsRepository> { MovieDetailsRepositoryImp(get(), get()) }
}

val useCaseModule = module {
    single { GetMoviesUseCase(get(), get(), get()) }
    single { GetMovieDetailsUseCase(get(), get(), get()) }
    single { GetPreferenceUseCase(get()) }
    single { GetFavoriteMovieUseCase(get(), get(), get()) }
    single { LoadNewMoviesUseCase(get(), get(), get()) }
    single { UpdateFavoriteMovieUseCase(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { MovieViewModel(get(), get(), get(), get()) }
}

val appModule = module {
    single { Handler(Looper.getMainLooper()) }
    single { Executors.newFixedThreadPool(4) }
    single { getDefaultSharedPreferences(androidApplication()) }
    single { NetworkHandler(androidApplication()) }
}

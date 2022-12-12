package com.example.topmovies.di

import android.app.Application
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.room.Room
import com.example.topmovies.database.MovieDatabase
import com.example.topmovies.database.dao.MovieDetailsDao
import com.example.topmovies.database.dao.MoviesDao
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.BASE_URL
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
    fun provideDefaultOkhttpClient(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS).build()

    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    fun provideRetrofit(client: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()

    fun provideMovieService(retrofit: Retrofit) = retrofit.create(MoviesApi::class.java)

    single { provideHttpLoggingInterceptor() }
    single { provideDefaultOkhttpClient(interceptor = get()) }
    single { provideRetrofit(client = get()) }
    single { provideMovieService(retrofit = get()) }
}

val viewModelModule = module {

    viewModel { MovieDetailsViewModel(get(), get(named("defPref"))) }
    viewModel { MovieViewModel(get(), get(named("defPref"))) }
}

val databaseModule = module {

    fun provideDatabase(app: Application): MovieDatabase =
        Room.databaseBuilder(app, MovieDatabase::class.java, DATABASE_NAME).build()

    fun provideDao(database: MovieDatabase): MoviesDao = database.movieDao()

    fun provideMoviesDetailsDao(database: MovieDatabase): MovieDetailsDao = database.movieDetails()

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { provideMoviesDetailsDao(get()) }
}

val repositoryModule = module {
    single { MovieRepository(get(), get(), get(), get()) }
}

val appModule = module {
    fun provideSharedPreference(app: Application) = getDefaultSharedPreferences(app)

    single { Executors.newSingleThreadExecutor() }
    single(named("defPref")) { provideSharedPreference(androidApplication()) }
}

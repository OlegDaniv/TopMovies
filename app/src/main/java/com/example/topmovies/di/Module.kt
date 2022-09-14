package com.example.topmovies.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.BASE_URL
import com.example.topmovies.unit.SHARED_PREFERENCE_NAME_FAVORITE
import com.example.topmovies.viewmodel.MovieViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideDefaultOkhttpClient() }
    single { provideRetrofit(client = get()) }
    single { provideMovieService(retrofit = get()) }
    single { MovieRepository(movieApi = get()) }
    single { provideSharedPreference(androidApplication()) }
    viewModel { MovieViewModel(repository = get(), sharedPref = get()) }
}

private fun provideDefaultOkhttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
}

private fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

private fun provideSharedPreference(app: Application): SharedPreferences = app.getSharedPreferences(
    SHARED_PREFERENCE_NAME_FAVORITE, Context.MODE_PRIVATE
)

private fun provideMovieService(retrofit: Retrofit): MoviesApi =
    retrofit.create(MoviesApi::class.java)

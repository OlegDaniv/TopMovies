package com.example.topmovies.di

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.BASE_URL
import com.example.topmovies.unit.SHARED_PREF_NAME_FAVORITE
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
import java.util.concurrent.TimeUnit

val appModule = module {
    single { provideHttpLoggingInterceptor() }
    single { provideDefaultOkhttpClient(interceptor = get()) }
    single { provideRetrofit(client = get()) }
    single { provideMovieService(retrofit = get()) }
    single { MovieRepository(movieApi = get()) }
    single(named("defPref")) { provideSharedPreference(androidApplication()) }
    single(named("favoritePref")) { provideFavoriteSharedPreference(androidApplication()) }
    viewModel {
        MovieViewModel(
            repository = get(),
            sharedPref = get(named("defPref")),
            favoritePref = get(named("favoritePref"))
        )
    }
    viewModel { MovieDetailsViewModel(repository = get(), sharedPref = get(named("defPref"))) }
}

private fun provideDefaultOkhttpClient(interceptor: HttpLoggingInterceptor) =
    OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS).build()

private fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun provideRetrofit(client: OkHttpClient) =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build()

private fun provideSharedPreference(app: Application) = getDefaultSharedPreferences(app)

private fun provideFavoriteSharedPreference(app: Application) = app.getSharedPreferences(
    SHARED_PREF_NAME_FAVORITE, Context.MODE_PRIVATE
)

private fun provideMovieService(retrofit: Retrofit) = retrofit.create(MoviesApi::class.java)

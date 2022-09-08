package com.example.topmovies.di

import android.content.Context
import com.example.topmovies.fragment.MovieViewModel
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.retrofit.MoviesApi
import com.example.topmovies.unit.BASE_URL
import com.example.topmovies.unit.SHARED_PREF_NAME_FAVORITE
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApi::class.java)
    }
    single { MovieRepository(movieApi = get()) }
    viewModel { MovieViewModel(repository = get()) }

    single {
        androidApplication().getSharedPreferences(SHARED_PREF_NAME_FAVORITE,
            Context.MODE_PRIVATE)
    }
}



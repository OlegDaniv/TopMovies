package com.example.topmovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.ActivityMainBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.viewmodel.ModelFactory
import com.example.topmovies.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private val adapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerview.adapter = adapter
        }
        ViewModelProvider(this, ModelFactory(MovieRepository()))[MovieViewModel::class.java]
            .apply {
                movieList.observe(this@MainActivity) {
                    adapter.apply {
                        setMovieList(it)
                        notifyItemRangeChanged(0, it.size)
                    }
                }
                getAllMovies()
            }
    }
}
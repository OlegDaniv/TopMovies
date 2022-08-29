package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.R
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.viewmodel.MovieModelFactory
import com.example.topmovies.viewmodel.MovieViewModel

class MoviesFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val moviesAdapter by lazy { MovieAdapter { id -> onClickItem(id) } }
    private val moviesViewModel by viewModels<MovieViewModel> {
        MovieModelFactory(MovieRepository())
    }

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolBarBridge?.hideUpButton()
        setupUI()
        setupViewModel()
    }

    private fun onClickItem(movieId: String) {
        startMovieDetailsFragment(movieId)
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            if (moviesViewModel.movies.value == null) getAllMovies()
            movies.observe(viewLifecycleOwner) {
                moviesAdapter.apply {
                    setMovieList(it)
                }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            recyclerviewMoviesFragment.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = moviesAdapter
            }
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.getAllMovies()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun startMovieDetailsFragment(movieId: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, MovieDetailsFragment.newInstance(movieId))
            .addToBackStack("movieList")
            .commit()
    }
}

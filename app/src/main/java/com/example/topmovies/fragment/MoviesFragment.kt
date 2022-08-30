package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.R
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.repository.MovieRepository

class MoviesFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val moviesAdapter by lazy { MovieAdapter { id -> onClickItem(id) } }
    private val moviesViewModel by activityViewModels<MovieViewModel> {
        MovieModelFactory(MovieRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideUpButton()
        setupUI()
        setupViewModel()
    }

    private fun onClickItem(movieId: String) {
        startMovieDetailsFragment(movieId)
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            if (moviesViewModel.movies.value == null) getMovies()
            movies.observe(viewLifecycleOwner) { moviesAdapter.setMovieList(it) }
        }
    }

    private fun setupUI() {
        binding.apply {
            recyclerviewMoviesFragment.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = moviesAdapter
            }
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.getMovies()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun startMovieDetailsFragment(movieId: String) {
        findNavController().navigate(
            R.id.action_navigation_top_movies_to_navigation_movie_details,
            bundleOf(MovieDetailsFragment.FRAGMENT_KEY to movieId)
        )
    }
}

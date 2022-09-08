package com.example.topmovies.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.R
import com.example.topmovies.adapter.MoviesAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val moviesAdapter by lazy { MoviesAdapter { id -> onClickItem(id) } }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    private val sharedPref: SharedPreferences by inject()

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

    override fun onDestroyView() {
        remove()
        moviesViewModel.favoriteMovies.value?.forEach { movie -> saveFavoriteMovie(movie.id) }
        super.onDestroyView()
    }

    private fun onClickItem(movieId: String) {
        startMovieDetailsFragment(movieId)
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            if (moviesViewModel.movies.value == null) getMovies(getApiKey(), getFavoriteMoviesId())
            movies.observe(viewLifecycleOwner) {
                it?.let { moviesAdapter.setMovieList(it) }
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
                moviesViewModel.getMovies(getApiKey(), getFavoriteMoviesId())
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

    private fun remove() = sharedPref.edit().clear().apply()

    private fun getFavoriteMoviesId() = sharedPref.all.keys.toList()

    private fun saveFavoriteMovie(key: String) = sharedPref.edit().putString(key, "").apply()
}

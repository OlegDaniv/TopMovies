package com.example.topmovies.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.MoviesAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseFragment() {
    
    private val binding by lazy { FragmentMoviesBinding.inflate(layoutInflater) }
    private val moviesAdapter by lazy { MoviesAdapter { id -> onClickItem(id) } }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    private val sharedPref: SharedPreferences by inject()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ) = binding.root
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideBackButton()
        setupUI()
        setupViewModel()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        removeMoviePreference()
        moviesViewModel.favoriteMovies.value?.forEach { movie -> saveFavoriteMovie(movie.id) }
    }

    private fun onClickItem(movieId: String) {
        startMovieDetailsFragment(movieId)
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            movies.value ?: resolveMovies(getFavoriteMoviesId())
            movies.observe(viewLifecycleOwner) {
                it?.let { moviesAdapter.setMovieList(it) }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            recyclerviewMoviesFragment.adapter = moviesAdapter
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.resolveMovies(getFavoriteMoviesId())
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
    
    private fun removeMoviePreference() = sharedPref.edit().clear().apply()
    
    private fun getFavoriteMoviesId() = sharedPref.all.keys.toList()
    
    private fun saveFavoriteMovie(key: String) = sharedPref.edit().putString(key, "").apply()
}

package com.example.topmovies.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.MoviesAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseFragment() {
    
    private val binding by lazy { FragmentMoviesBinding.inflate(layoutInflater) }
    private val moviesAdapter by lazy { MoviesAdapter { id -> onClickItem(id) } }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    
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
        moviesViewModel.removeMoviePreference()
        moviesViewModel.saveFavoriteMovie()
    }

    private fun onClickItem(movieId: String) {
        startMovieDetailsFragment(movieId)
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            movies.value ?: resolveMovies(getFavoriteMoviesId())
            if (moviesViewModel.movies.value == null) getMovies(getApiKey(), getFavoriteMoviesId())
            movies.observe(viewLifecycleOwner) {
                it?.let { moviesAdapter.setMovieList(it) }
            }
        }
        moviesViewModel.errorMassage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUI() {
        binding.apply {
            recyclerviewMovies.adapter = moviesAdapter
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.resolveMovies(moviesViewModel.getFavoriteMoviesId())
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
}

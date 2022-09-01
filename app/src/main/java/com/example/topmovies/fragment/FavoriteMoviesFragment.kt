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
import com.example.topmovies.adapter.FavoriteMoviesAdapter
import com.example.topmovies.databinding.FragmentFavoriteMoviesBinding
import com.example.topmovies.preferences.SharedPref
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.SHARED_PREFERENCE_NAME_FAVORITE

class FavoriteMoviesFragment : BaseFragment() {
    private lateinit var binding: FragmentFavoriteMoviesBinding
    private val favoriteMoviesAdapter by lazy { FavoriteMoviesAdapter { id -> onItemClick(id) } }
    private val sharedPref by lazy {
        SharedPref(requireActivity().baseContext, SHARED_PREFERENCE_NAME_FAVORITE)
    }
    private val moviesViewModel by activityViewModels<MovieViewModel> {
        MovieModelFactory(MovieRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideUpButton()
        setupUI()
        setupViewModel()
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            getFavoriteMovies(sharedPref.allFavoriteMoviesId())
            favoriteMovies.observe(viewLifecycleOwner) {
                favoriteMoviesAdapter.setFavoriteMovies(it)
            }
        }
    }

    private fun setupUI() {
        binding.recyclerviewFavoriteMoviesFragment.apply {
            adapter = favoriteMoviesAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun startMovieDetailsFragment(id: String) {
        findNavController().navigate(
            R.id.action_navigation_favorite_movies_to_navigation_movie_details,
            bundleOf(MovieDetailsFragment.FRAGMENT_KEY to id)
        )
    }

    private fun onItemClick(movieID: String) {
        startMovieDetailsFragment(movieID)
    }
}

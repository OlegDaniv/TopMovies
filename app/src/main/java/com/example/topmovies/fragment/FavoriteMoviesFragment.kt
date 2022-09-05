package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.R
import com.example.topmovies.adapter.FavoriteMoviesAdapter
import com.example.topmovies.databinding.FragmentFavoriteMoviesBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoriteMoviesFragment : BaseFragment() {
    private lateinit var binding: FragmentFavoriteMoviesBinding
    private val favoriteMoviesAdapter by lazy { FavoriteMoviesAdapter { id -> onItemClick(id) } }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()

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
            getFavoriteMovies()
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

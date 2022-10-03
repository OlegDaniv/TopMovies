package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.FavoriteMoviesAdapter
import com.example.topmovies.databinding.FragmentFavoriteMoviesBinding
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoriteMoviesFragment : Fragment() {
    
    private val binding by lazy { FragmentFavoriteMoviesBinding.inflate(layoutInflater) }
    private val favoriteMoviesAdapter by lazy {
        FavoriteMoviesAdapter { id -> startMovieDetailsFragment(id) }
    }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ) = binding.root
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupViewModel()
    }
    
    private fun setupViewModel() {
        moviesViewModel.resolveFavoriteMovies()
        moviesViewModel.favoriteMovies.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.recyclerviewFavoriteMovies.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                favoriteMoviesAdapter.setFavoriteMovies(it)
                binding.recyclerviewFavoriteMovies.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }
        }
    }
    
    private fun setupRecyclerView() {
        binding.recyclerviewFavoriteMovies.adapter = favoriteMoviesAdapter
    }
    
    private fun startMovieDetailsFragment(id: String) {
        findNavController().navigate(
            R.id.action_favorite_movies_to_movie_details, bundleOf(FRAGMENT_KEY to id)
        )
    }
}

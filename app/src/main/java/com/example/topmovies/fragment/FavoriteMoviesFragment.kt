package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.FavoriteMoviesAdapter
import com.example.topmovies.databinding.FragmentFavoriteMoviesBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoriteMoviesFragment : BaseFragment() {
    
    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!
    private val favoriteMoviesAdapter by lazy {
        FavoriteMoviesAdapter(
            { id -> startMovieDetailsFragment(id) },
            { movie -> removeFavoriteMovie(movie) }
        )
    }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupViewModel()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun removeFavoriteMovie(movie: Movie) {
        moviesViewModel.removeFavoriteMovie(movie)
    }
    
    private fun setupViewModel() {
        with(moviesViewModel) {
            resolveFavoriteMovies()
            favoriteMovies.observe(viewLifecycleOwner) {
                favoriteMoviesAdapter.submitFavoriteMovies(it)
                showMovieList(it)
            }
        }
    }
    
    private fun showMovieList(movies: List<Movie>) {
        if (movies.isEmpty()) {
            binding.recyclerviewFavoriteMovies.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerviewFavoriteMovies.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
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

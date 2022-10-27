package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.MoviesAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.unit.ALL_MOVIES_SCREEN
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseFragment() {
    
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    private val moviesAdapter by lazy {
        MoviesAdapter(
            ::startMovieDetailsFragment,
            ::favoriteMovieClicked
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        setupViewModel()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        moviesAdapter.submitMoviesList(emptyList())
    }
    
    private fun setupViewModel() = with(moviesViewModel) {
        movies.value ?: resolveMovies()
        movies.observe(viewLifecycleOwner) {
            moviesAdapter.submitMoviesList(it)
            binding.swipeRefresh.isRefreshing = false
        }
        errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                showErrorMassage(it)
                errorMessage.value = null
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }
    
    private fun favoriteMovieClicked(movie: Movie) {
        moviesViewModel.addFavoriteMovie(movie, ALL_MOVIES_SCREEN)
    }
    
    private fun setupUI() = with(binding) {
        recyclerviewMovies.adapter = moviesAdapter
        swipeRefresh.setOnRefreshListener {
            moviesViewModel.resolveMovies()
        }
    }
    
    private fun startMovieDetailsFragment(movieId: String) {
        findNavController().navigate(
            R.id.action_top_movies_to_movie_details,
            bundleOf(FRAGMENT_KEY to movieId),
        )
    }
}

package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.MoviesAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.unit.ALL_MOVIES_SCREEN
import com.example.topmovies.unit.FAVOURITE_MOVIES_SCREEN
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseFragment() {
    
    private var _binding: FragmentMoviesBinding? = null
    private val screen by lazy { arguments?.getInt("Screen") ?: ALL_MOVIES_SCREEN }
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
        setupUI(screen)
        setupViewModel(screen)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        moviesAdapter.submitMoviesList(screen, emptyList())
    }
    
    private fun setupViewModel(screen: Int) = with(moviesViewModel) {
        getMovies()
        getMoviesList(screen).observe(viewLifecycleOwner) {
            moviesAdapter.submitMoviesList(screen, it)
            showMovieList(it)
        }

        errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                showErrorMassage(it)
                errorMessage.value = null
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }
    
    private fun showMovieList(movies: List<Movie>) = with(binding) {
        recyclerviewMovies.isVisible = movies.isNotEmpty()
        emptyView.isVisible = movies.isEmpty()
    }
    
    private fun favoriteMovieClicked(movie: Movie) {
        moviesViewModel.addFavoriteMovie(movie, screen)
    }
    
    private fun setupUI(screen: Int) = with(binding) {
        recyclerviewMovies.adapter = moviesAdapter
        when (screen) {
            ALL_MOVIES_SCREEN -> {
                swipeRefresh.setOnRefreshListener {
                    moviesViewModel.resolveMovies()
                    swipeRefresh.isRefreshing = false
                }
            }
            FAVOURITE_MOVIES_SCREEN -> {
                swipeRefresh.isRefreshing = false
                swipeRefresh.isEnabled = false
            }
        }
    }
    
    private fun startMovieDetailsFragment(movieId: String) {
        findNavController().navigate(
            R.id.action_top_movies_to_movie_details,
            bundleOf(FRAGMENT_KEY to movieId),
        )
    }
}

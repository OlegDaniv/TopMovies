package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.topmovies.R
import com.example.topmovies.adapter.MoviesAdapter
import com.example.topmovies.databinding.FragmentMoviesBinding
import com.example.topmovies.unit.NETWORK_ERROR
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : Fragment() {
    
    private val binding by lazy { FragmentMoviesBinding.inflate(layoutInflater) }
    private val moviesAdapter by lazy { MoviesAdapter { id -> startMovieDetailsFragment(id) } }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ) = binding.root
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        setupViewModel()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        moviesViewModel.removeMoviePreference()
        moviesViewModel.saveFavoriteMovie()
    }

    private fun setupViewModel() {
        moviesViewModel.apply {
            movies.value ?: resolveMovies(getFavoriteMoviesId())
            movies.observe(viewLifecycleOwner) {
                it?.let { moviesAdapter.setMovieList(it) }
            }
            errorMessage.observe(viewLifecycleOwner) {
                it?.let {
                    if (it.startsWith(NETWORK_ERROR)) {
                        NetworkDialogFragment().show(requireActivity().supportFragmentManager, null)
                        errorMessage.value = null
                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        errorMessage.value = null
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            recyclerviewMovies.adapter = moviesAdapter
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.resolveMovies(moviesViewModel.getFavoriteMoviesId())
                swipeRefresh.isRefreshing = false
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

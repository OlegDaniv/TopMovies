package com.example.topmovies.fragment

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
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
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : Fragment() {
    
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val moviesAdapter by lazy { MoviesAdapter { id -> startMovieDetailsFragment(id) } }
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    
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
    
    override fun onStop() {
        super.onStop()
        moviesViewModel.resolveFavoriteMovies()
        moviesViewModel.removeMoviePreference()
        moviesViewModel.saveFavoriteMovie()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun setupViewModel() {
        moviesViewModel.apply {
            movies.value ?: resolveMovies()
            movies.observe(viewLifecycleOwner) {
                it?.let { moviesAdapter.setMovieList(it) }
            }
            errorMessage.observe(viewLifecycleOwner) {
                it?.let {
                    if (isNetworkAvailable()) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    } else {
                        NetworkDialogFragment().show(parentFragmentManager, null)
                    }
                    errorMessage.value = null
                }
            }
        }
    }
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NET_CAPABILITY_INTERNET))
    }
    
    private fun setupUI() {
        binding.apply {
            recyclerviewMovies.adapter = moviesAdapter
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.resolveMovies()
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

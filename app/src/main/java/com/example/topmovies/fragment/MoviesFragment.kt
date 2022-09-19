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
import com.example.topmovies.model.MovieEntity
import com.example.topmovies.unit.EnumScreen
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseFragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val screen by lazy {
        arguments?.getSerializable(resources.getString(R.string.notification_argument_name)) as EnumScreen
    }
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
        moviesAdapter.submitMoviesList(emptyList())
    }

    private fun setupViewModel(screen: EnumScreen) = with(moviesViewModel) {
        getMovies()
        getMoviesList(screen).observe(viewLifecycleOwner) {
            moviesAdapter.submitMoviesList(it)
            showMovieList(it, screen)
        }

        errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                showErrorMassage(it)
                errorMessage.value = null
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun showMovieList(movies: List<MovieEntity>, screen: EnumScreen) = with(binding) {
        when (screen) {
            EnumScreen.MOVIES -> emptyView.text =
                resources.getString(R.string.movies_massage_no_movies)
            EnumScreen.FAVORITE -> emptyView.text =
                resources.getString(R.string.favorite_movies_massage_no_movies)
        }
        recyclerviewMovies.isVisible = movies.isNotEmpty()
        emptyView.isVisible = movies.isEmpty()
    }

    private fun favoriteMovieClicked(id: String, favorite: Boolean) {
        moviesViewModel.addFavoriteMovie(id, favorite, screen)
    }

    private fun setupUI(screen: EnumScreen) = with(binding) {
        recyclerviewMovies.adapter = moviesAdapter
        when (screen) {
            EnumScreen.MOVIES -> {
                swipeRefresh.setOnRefreshListener {
                    moviesViewModel.resolveMovies()
                    swipeRefresh.isRefreshing = false
                }
            }
            EnumScreen.FAVORITE -> {
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

package com.example.topmovies.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.presentation.utils.GlideApp
import com.example.topmovies.presentation.viewmodels.MovieDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(FRAGMENT_KEY)?.let { loadMovieDetailsById(it) }
        setupViewModel()
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        movieViewModel.errorMessage.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
    }

    private fun setupUI() = with(binding) {
        movieViewModel.movieDetails.observe(viewLifecycleOwner) {
            textviewMovieDetailsDescription.text = it.plot
            GlideApp.with(imageviewMovieDetailsImage).asBitmap().load(it.imageUrl)
                .into(imageviewMovieDetailsImage)
            textviewMovieDetailsMovieTitle.text = it.title
            textviewMovieDetailsRatingNumber.text = it.imDbRating
            textviewMovieDetailsGenresSource.text = it.genres
            textviewMovieDetailsDateReleaseSource.text = it.releaseDate
            errorLine.text = it.errorMessage
        }
    }

    private fun loadMovieDetailsById(movieId: String) {
        movieViewModel.resolveMovieDetails(movieId)
    }

    companion object {
        const val FRAGMENT_KEY = "movieID"
    }
}

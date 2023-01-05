package com.example.topmovies.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.presentation.models.MovieDetails
import com.example.topmovies.presentation.utils.GlideApp
import com.example.topmovies.presentation.viewmodel.MovieDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : BaseFragment() {

    companion object {
        const val ID_ARGUMENT_KEY = "id_argument_key"
    }

    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(ID_ARGUMENT_KEY)?.let { loadMovieDetailsById(it) }
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            handlerFailure(it)
        }
        viewModel.movieDetails.observe(
            viewLifecycleOwner,
            ::renderMovieDetails
        )
    }

    private fun renderMovieDetails(movieDetails: MovieDetails) = with(binding) {
        textviewMovieDetailsDescription.text = movieDetails.plot
        GlideApp.with(imageviewMovieDetailsImage).asBitmap().load(movieDetails.imageUrl)
            .into(imageviewMovieDetailsImage)
        textviewMovieDetailsMovieTitle.text = movieDetails.title
        textviewMovieDetailsRatingNumber.text = movieDetails.imDbRating
        textviewMovieDetailsGenresSource.text = movieDetails.genres
        textviewMovieDetailsDateReleaseSource.text = movieDetails.releaseDate
        errorLine.text = movieDetails.errorMessage
    }

    private fun loadMovieDetailsById(movieId: String) {
        viewModel.resolveMovieDetails(movieId)
    }
}

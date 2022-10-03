package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.unit.NETWORK_ERROR
import com.example.topmovies.viewmodel.MovieDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val FRAGMENT_KEY = "movieID"

class MovieDetailsFragment : Fragment() {
    
    private val binding by lazy { FragmentDetailsMovieBinding.inflate(layoutInflater) }
    private val movieViewModel: MovieDetailsViewModel by viewModel()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(FRAGMENT_KEY)?.let { loadMovieDetailsById(it) }
        setupViewModel()
        setupUI()
    }
    
    private fun setupViewModel() {
        movieViewModel.movieDetails.observe(viewLifecycleOwner) {
            requireArguments().getString(FRAGMENT_KEY)?.let { loadMovieDetailsById(it) }
        }
        movieViewModel.detailsErrorMassage.observe(viewLifecycleOwner) {
            if (it.startsWith(NETWORK_ERROR)) {
                NetworkDialogFragment().show(requireActivity().supportFragmentManager, null)
            } else {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupUI() {
        binding.apply {
            movieViewModel.movieDetails.observe(viewLifecycleOwner) {
                textviewMovieDetailsDescription.text = it.plot
                Glide.with(imageviewMovieDetailsImage).asBitmap().load(it.imageUrl)
                    .into(imageviewMovieDetailsImage)
                textviewMovieDetailsMovieTitle.text = it.title
                textviewMovieDetailsRatingNumber.text = it.imDbRating
                textviewMovieDetailsGenresSource.text = it.genres
                textviewMovieDetailsDateReleaseSource.text = it.releaseDate
                errorLine.text = it.errorMessage
            }
        }
    }

    private fun loadMovieDetailsById(movieId: String) {
        movieViewModel.resolveMovieDetails(movieId)
    }
}

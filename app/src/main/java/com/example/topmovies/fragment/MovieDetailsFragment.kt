package com.example.topmovies.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.topmovies.R
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.viewmodel.MovieDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val FRAGMENT_KEY = "movieID"

class MovieDetailsFragment : Fragment(R.layout.fragment_details_movie) {
    
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
        movieViewModel.movieDetails.observe(viewLifecycleOwner) {
            requireArguments().getString(FRAGMENT_KEY)?.let { loadMovieDetailsById(it) }
        }
        movieViewModel.detailsErrorMassage.observe(viewLifecycleOwner) {
            if (isNetworkAvailable()) {
                NetworkDialogFragment().show(parentFragmentManager, null)
            } else {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
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

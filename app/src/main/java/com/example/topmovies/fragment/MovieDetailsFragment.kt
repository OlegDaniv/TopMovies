package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MovieDetailsFragment : Fragment() {
    
    private val binding by lazy { FragmentDetailsMovieBinding.inflate(layoutInflater) }
    private val movieViewModel by sharedViewModel<MovieViewModel>()
    
    companion object {
        const val FRAGMENT_KEY = "movieID"
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        requireArguments().getString(FRAGMENT_KEY)?.let { loadMovieDetailsById(it) }
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
            }
        }
    }

    private fun loadMovieDetailsById(movieId: String) {
        movieViewModel.resolveMovieDetails(movieId)
    }
}

package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.repository.MovieRepository


class MovieDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailsMovieBinding
    private val viewModelMovieDetailsFragment by viewModels<MovieViewModel> {
        MovieModelFactory(MovieRepository())
    }

    companion object {
        const val FRAGMENT_KEY = "movieID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val movieId = requireArguments().getString(FRAGMENT_KEY)
        showUpButton()
        setupUI()
        movieId?.let { loadMovieDetailsById(it) }
    }

    private fun setupUI() {
        binding.apply {
            viewModelMovieDetailsFragment.movieDetails.observe(viewLifecycleOwner) {
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
        viewModelMovieDetailsFragment.getMovieDetails(movieId)
    }
}

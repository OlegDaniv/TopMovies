package com.example.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.topmovies.databinding.FragmentDetailsMovieBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.viewmodel.MovieModelFactory
import com.example.topmovies.viewmodel.MovieViewModel

const val FRAGMENT_KEY = "movieID"

class MovieDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailsMovieBinding
    private val movieId: String by lazy { arguments?.getString(FRAGMENT_KEY) ?: "" }
    private val viewModelMovieDetailsFragment by viewModels<MovieViewModel> {
        MovieModelFactory(MovieRepository())
    }

    companion object {
        fun newInstance(movieId: String): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(FRAGMENT_KEY, movieId)
                }
            }
        }
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
        toolBarBridge?.showUpButton()
        setupUI()
        loadMovieDetailsById()
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

    private fun loadMovieDetailsById() {
        viewModelMovieDetailsFragment.getMovieDetails(movieId)
    }
}

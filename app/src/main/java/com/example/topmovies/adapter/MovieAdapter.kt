package com.example.topmovies.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.topmovies.R
import com.example.topmovies.databinding.ItemLayoutBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.unit.IMAGE_SIZE
import com.example.topmovies.unit.REPLACE_AFTER

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {

    private var movies = listOf<Movie>()

    fun setMovieList(movies: List<Movie>) {
        this.movies = movies
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder(
        ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
        holder.bind(movies[position])

    override fun getItemCount() = movies.size

    class MainViewHolder(
        private val binding: ItemLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val avatarCustomTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap, transition: Transition<in Bitmap>?
            ) {
                binding.circleAvatarView.setAvatarImage(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        }

        fun bind(movie: Movie) {
            binding.apply {
                textviewMovieName.text = movie.title
                textviewMovieRankName.text = movie.rank
                textviewMovieYearName.text = movie.year
                circleAvatarView.setLabel(movie.title)
                Glide.with(circleAvatarView)
                    .asBitmap()
                    .load(resizeImage(movie.imageUrl))
                    .error(R.drawable.empty_image)
                    .into(avatarCustomTarget)
            }
        }

        private fun resizeImage(image: String) =
            image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)
    }
}

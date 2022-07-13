package com.example.topmovies.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.topmovies.databinding.ItemLayoutBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.unit.IMAGE_SIZE
import com.example.topmovies.unit.REPLACE_AFTER

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {
    var movies = mutableListOf<Movie>()
    fun setMovieList(movies: List<Movie>) {
        this.movies = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MainViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun resizeImage(image: String): String {
            return image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)
        }

        fun bind(position: Int) {
            val movie = movies[position]
            val movieImageResize = resizeImage(movie.image)
            binding.apply {
                textviewMovieName.text = movie.title
                textviewMovieRankName.text = movie.rank
                textviewMovieYearName.text = movie.year
                circleAvatarView.addLetterInCircleAvatar(movie.title)
            }
            Glide.with(binding.circleAvatarView).asBitmap().load(movieImageResize)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        binding.circleAvatarView.changeAvatarImage(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }
}

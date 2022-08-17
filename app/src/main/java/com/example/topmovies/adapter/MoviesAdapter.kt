package com.example.topmovies.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.topmovies.R
import com.example.topmovies.databinding.ItemLayoutBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.unit.IMAGE_SIZE
import com.example.topmovies.unit.REPLACE_AFTER

class MovieAdapter(private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = listOf<Movie>()

    fun setMovieList(movies: List<Movie>) {
        this.movies = movies
        notifyItemRangeChanged(0, movies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onItemClickListener
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(movies[position])

    override fun getItemCount() = movies.size

    class MovieViewHolder(
        private val binding: ItemLayoutBinding,
        onItemClickListener: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val click = onItemClickListener
        private val avatarCustomTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                binding.circleAvatarViewItemLayoutMovieImage.setAvatarImage(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        }

        fun bind(movie: Movie) {
            binding.apply {
                textviewItemLayoutMovieName.text = movie.title
                textviewItemLayoutRankNumber.text = movie.rank
                textviewItemLayoutYearNumber.text = movie.year
                textviewItemLayoutPreviousRankNumber.text = movie.rankUpDown
                when (movie.rankUpDown.first()) {
                    '+' -> textviewItemLayoutPreviousRankNumber.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.text_rank_up)
                    )
                    '-' -> textviewItemLayoutPreviousRankNumber.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.text_rank_down)
                    )
                }
                circleAvatarViewItemLayoutMovieImage.setLabel(movie.title)
                Glide.with(circleAvatarViewItemLayoutMovieImage)
                    .asBitmap()
                    .load(resizeImage(movie.imageUrl))
                    .into(avatarCustomTarget)
            }
            itemView.setOnClickListener {
                click(movie.id)
            }
        }

        private fun resizeImage(image: String) =
            image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)
    }
}

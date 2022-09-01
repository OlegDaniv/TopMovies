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
import com.example.topmovies.preferences.SharedPref
import com.example.topmovies.unit.*

class MoviesAdapter(private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

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
        private val onItemClickListener: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val sharedPref by lazy {
            SharedPref(itemView.context, SHARED_PREFERENCE_NAME_FAVORITE)
        }
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
                circleAvatarViewItemLayoutMovieImage.setLabel(movie.title)
                Glide.with(circleAvatarViewItemLayoutMovieImage)
                    .asBitmap()
                    .load(resizeImage(movie.imageUrl))
                    .into(avatarCustomTarget)
                imageButtonItemFavoriteIcon.setImageResource(movieIsFavorite(movie.id))
                setRankUpDownColor(movie.rankUpDown)
            }
            itemView.setOnClickListener { onItemClickListener(movie.id) }
            setOnClickListenerFavoriteButton(movie.id)
        }

        private fun setRankUpDownColor(rankUpDown: String) {
            binding.textviewItemLayoutPreviousRankNumber.setTextColor(
                ContextCompat.getColor(itemView.context,
                    when (rankUpDown.first()) {
                        RANK_UP -> R.color.text_rank_up
                        RANK_DOWN -> R.color.text_rank_down
                        else -> R.color.text_color_fresh_ivy_green
                    }
                ))
        }

        private fun setOnClickListenerFavoriteButton(id: String) {
            binding.imageButtonItemFavoriteIcon.setOnClickListener {
                if (sharedPref.movieIsFavorite(id)) {
                    sharedPref.saveFavoriteMovie(id, false)
                    binding.imageButtonItemFavoriteIcon.setImageResource(R.drawable.ic_filled_star)
                } else {
                    sharedPref.removeFavoriteMovie(id)
                    binding.imageButtonItemFavoriteIcon.setImageResource(R.drawable.ic_unfilled_star)
                }
            }
        }

        private fun movieIsFavorite(id: String): Int {
            return if (sharedPref.movieIsFavorite(id)) R.drawable.ic_unfilled_star
            else R.drawable.ic_filled_star
        }

        private fun resizeImage(image: String) = image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)
    }
}

package com.example.topmovies.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
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

class FavoriteMoviesAdapter(private val recyclerviewClickInterface: RecyclerviewClickInterface) :
    RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder>() {
    private val onClick = recyclerviewClickInterface
    private var favoriteMovies = mutableListOf<Movie>()

    fun setFavoriteMovies(movies: MutableList<Movie>) {
        favoriteMovies = movies
        notifyItemRangeChanged(0, movies.size)
    }

    private fun removeMovie(movie: Movie) {
        val d = favoriteMovies.indexOf(movie)
        favoriteMovies.toMutableList().removeAt(1)
        notifyItemRemoved(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteMoviesViewHolder(binding, onClick, ::removeMovie)
    }

    override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) {
        holder.bind(favoriteMovies[position])
    }


    override fun getItemCount() = favoriteMovies.size


    class FavoriteMoviesViewHolder(
        private val binding: ItemLayoutBinding, d: RecyclerviewClickInterface,
        val fff: (Movie) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        val s = binding.imageButtonItemFavoriteIcon
        private val d = d
        private val sharedPref by lazy {
            SharedPref(itemView.context, SHARED_PREFERENCE_NAME_FAVORITE)
        }

        private fun resizeImage(image: String) = image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)
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
            itemView.setOnClickListener {
                d.onItemClick(movie.id)
            }

            binding.imageButtonItemFavoriteIcon.setOnClickListener {
                fff(movie)
                sharedPref.removeFavoriteMovie(movie.id)
            }

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

        private fun movieIsFavorite(id: String): Int {
            return if (sharedPref.movieIsFavorite(id)) R.drawable.ic_unfilled_star
            else R.drawable.ic_filled_star
        }
    }
}

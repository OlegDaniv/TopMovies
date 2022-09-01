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

class FavoriteMoviesAdapter(private val onClickFavoriteMovie: (String) -> Unit) :
    RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder>() {
    private var favoriteMovies = listOf<Movie>()

    fun setFavoriteMovies(movies: List<Movie>) {
        favoriteMovies = movies
        notifyItemRangeChanged(0, movies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteMoviesViewHolder(binding, onClickFavoriteMovie, ::removeMovie)
    }

    override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) =
        holder.bind(favoriteMovies[position])

    override fun getItemCount() = favoriteMovies.size

    private fun removeMovie(movie: Movie) {
        val index = favoriteMovies.indexOf(movie)
        val mutableList = favoriteMovies.toMutableList()
        mutableList.removeAt(index)
        favoriteMovies = mutableList.toList()
        notifyItemRemoved(index)
    }


    class FavoriteMoviesViewHolder(
        private val binding: ItemLayoutBinding,
        private val onClickFavoriteMovie: (String) -> Unit,
        private val removeListener: (Movie) -> Unit,
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
                imageButtonItemFavoriteIcon.setImageResource(R.drawable.ic_filled_star)
                setRankUpDownColor(movie.rankUpDown)
                imageButtonItemFavoriteIcon.setOnClickListener {
                    removeListener(movie)
                    sharedPref.removeFavoriteMovie(movie.id)
                }
            }
            itemView.setOnClickListener { onClickFavoriteMovie(movie.id) }
        }

        private fun resizeImage(image: String) = image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)

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
    }
}

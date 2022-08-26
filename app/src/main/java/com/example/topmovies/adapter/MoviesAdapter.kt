package com.example.topmovies.adapter

import android.content.Context
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
import com.example.topmovies.unit.SHARED_PREFERENCE_NAME_FAVORITE

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
            this.binding.apply {
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
                imageButtonItemFavoriteIcon.setImageResource(getIsFavorite(movie.id))
            }
            itemView.setOnClickListener { click(movie.id) }
            clickFavorite(movie)
        }

        private fun clickFavorite(movie: Movie) {
            binding.imageButtonItemFavoriteIcon.apply {
                setOnClickListener {
                    if (readState(movie.id)) {
                        savePreferenceState(movie.id, false)
                        setImageResource(R.drawable.ic_filled_star)
                    } else {
                        removePreferenceState(movie.id)
                        setImageResource(R.drawable.ic_unfilled_star)
                    }
                }
            }
        }

        private fun getIsFavorite(id: String): Int {
            val isFavorite = readState(id)
            return if (isFavorite) {
                R.drawable.ic_unfilled_star
            } else {
                R.drawable.ic_filled_star
            }
        }

        private fun removePreferenceState(id: String) {
            val favoritePreference = initializeSharedPreference(SHARED_PREFERENCE_NAME_FAVORITE)
            favoritePreference.edit().remove(id).apply()
        }

        private fun savePreferenceState(id: String, favorite: Boolean) {
            val favoritePreference = initializeSharedPreference(SHARED_PREFERENCE_NAME_FAVORITE)
            favoritePreference.edit().putBoolean(id, favorite).apply()
        }

        private fun readState(id: String): Boolean {
            val sharedPreferences = initializeSharedPreference(SHARED_PREFERENCE_NAME_FAVORITE)
            return sharedPreferences.getBoolean(id, true)
        }

        private fun initializeSharedPreference(name: String) =
            itemView.context.getSharedPreferences(name, Context.MODE_PRIVATE)

        private fun resizeImage(image: String) =
            image.replaceAfter(REPLACE_AFTER, IMAGE_SIZE)
    }
}

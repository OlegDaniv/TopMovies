package com.example.topmovies.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.topmovies.R
import com.example.topmovies.databinding.ItemLayoutBinding
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.unit.IMAGE_SIZE
import com.example.topmovies.unit.RANK_DOWN
import com.example.topmovies.unit.RANK_UP
import com.example.topmovies.unit.REPLACE_AFTER
import com.example.topmovies.utils.GlideApp

class MoviesAdapter(
    private val onItemClickListener: (String) -> Unit,
    private val onFavoriteMovieClick: (String, Boolean) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffCallBack()) {

    fun submitMoviesList(movies: List<Movie>) {
        submitList(movies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onItemClickListener,
        onFavoriteMovieClick
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                holder.bindChances(it as PayloadChange)
            }
        }
    }

    class MovieViewHolder(
        private val binding: ItemLayoutBinding,
        private val onItemClickListener: (String) -> Unit,
        private val onFavoriteMovieClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val avatarCustomTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                binding.circleAvatarViewItemLayoutMovieImage.setAvatarImage(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        }

        fun bindChances(changes: PayloadChange) =
            when (changes) {
                is PayloadChange.Both -> {
                    getChanges(changes.id, changes.isFavorite)
                    newRank(changes.rankUpDown)
                }
                is PayloadChange.Favorite -> getChanges(changes.id, changes.isFavorite)
                is PayloadChange.Rank -> newRank(changes.rankUpDown)

            }

        private fun newRank(rankUpDown: String) {
            binding.textviewItemLayoutRankNumber.text = rankUpDown
        }

        private fun getChanges(id: String, favorite: Boolean) {
            with(binding.imageButtonItemFavoriteIcon) {
                icon = AppCompatResources.getDrawable(
                    binding.root.context,
                    getFavoriteImageResource(favorite)
                )
                setOnClickListener {
                    onFavoriteMovieClick(id, favorite)
                }
            }
        }

        fun bind(movie: Movie) = with(binding) {
            textviewItemLayoutMovieName.text = movie.title
            textviewItemLayoutRankNumber.text = movie.rank
            textviewItemLayoutYearNumber.text = movie.year
            textviewItemLayoutPreviousRankNumber.text = movie.rankUpDown
            circleAvatarViewItemLayoutMovieImage.setLabel(movie.title)
            GlideApp.with(circleAvatarViewItemLayoutMovieImage)
                .asBitmap()
                .load(movie.imageUrl.replaceAfter(REPLACE_AFTER, IMAGE_SIZE))
                .into(avatarCustomTarget)
            imageButtonItemFavoriteIcon.apply {
                icon = AppCompatResources.getDrawable(
                    context, getFavoriteImageResource(movie.isFavorite)
                )
                setOnClickListener {
                    onFavoriteMovieClick(movie.id, movie.isFavorite)
                }
                setRankUpDownColor(movie.rankUpDown)
            }
            itemView.setOnClickListener { onItemClickListener(movie.id) }
        }

        private fun setRankUpDownColor(rankUpDown: String) {
            binding.textviewItemLayoutPreviousRankNumber.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    when (rankUpDown.first()) {
                        RANK_UP -> R.color.md_theme_light_tertiary
                        RANK_DOWN -> R.color.md_theme_light_error
                        else -> R.color.md_theme_dark_inverseSurface
                    }
                )
            )
        }

        private fun getFavoriteImageResource(isFavorite: Boolean): Int {
            return if (isFavorite) R.drawable.ic_filled_star
            else R.drawable.ic_unfilled_star
        }
    }

    private class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return ((oldItem.isFavorite == newItem.isFavorite) &&
                    (oldItem.rank == newItem.rank) &&
                    (oldItem.imageUrl == newItem.imageUrl) &&
                    (oldItem.title == newItem.title) &&
                    (oldItem.rankUpDown == newItem.rankUpDown) &&
                    (oldItem.year == newItem.year))
        }

        override fun getChangePayload(oldItem: Movie, newItem: Movie): Any? {
            if (oldItem.isFavorite != newItem.isFavorite && oldItem.rank != newItem.rank) {
                return PayloadChange.Both(newItem.id, newItem.isFavorite, newItem.rank)
            } else if (oldItem.isFavorite != newItem.isFavorite) {
                return PayloadChange.Favorite(newItem.id, newItem.isFavorite)
            } else if (oldItem.rank != newItem.rank) {
                return PayloadChange.Rank(newItem.rank)
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }

    sealed class PayloadChange {
        class Favorite(val id: String, val isFavorite: Boolean) : PayloadChange()
        class Rank(val rankUpDown: String) : PayloadChange()
        class Both(val id: String, val isFavorite: Boolean, val rankUpDown: String) :
            PayloadChange()
    }
}
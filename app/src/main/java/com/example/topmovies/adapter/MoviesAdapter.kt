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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.topmovies.R
import com.example.topmovies.databinding.ItemLayoutBinding
import com.example.topmovies.model.Movie
import com.example.topmovies.unit.IMAGE_SIZE
import com.example.topmovies.unit.RANK_DOWN
import com.example.topmovies.unit.RANK_UP
import com.example.topmovies.unit.REPLACE_AFTER

class MoviesAdapter(
    private val onItemClickListener: (String) -> Unit,
    private val onFavoriteMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffCallBack()) {
    
    fun submitMoviesList(movies: List<Movie>) = submitList(movies)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onItemClickListener,
        onFavoriteMovieClick,
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
            if (payloads[0] == true) {
                holder.bindFavoriteState(getItem(position))
            }
        }
    }
    
    class MovieViewHolder(
        private val binding: ItemLayoutBinding,
        private val onItemClickListener: (String) -> Unit,
        private val onFavoriteMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private val avatarCustomTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                binding.circleAvatarViewItemLayoutMovieImage.setAvatarImage(resource)
            }
            
            override fun onLoadCleared(placeholder: Drawable?) {}
        }
        
        fun bindFavoriteState(movie: Movie) {
            
            binding.imageButtonItemFavoriteIcon.icon =
                AppCompatResources.getDrawable(
                    binding.root.context,
                    getFavoriteImageResource(movie.isFavorite)
                )
        }
        
        fun bind(movie: Movie) = with(binding) {
            textviewItemLayoutMovieName.text = movie.title
            textviewItemLayoutRankNumber.text = movie.rank
            textviewItemLayoutYearNumber.text = movie.year
            textviewItemLayoutPreviousRankNumber.text = movie.rankUpDown
            circleAvatarViewItemLayoutMovieImage.setLabel(movie.title)
            Glide.with(circleAvatarViewItemLayoutMovieImage)
                .asBitmap()
                .load(movie.imageUrl.replaceAfter(REPLACE_AFTER, IMAGE_SIZE))
                .into(avatarCustomTarget)
            imageButtonItemFavoriteIcon.apply {
                icon = AppCompatResources.getDrawable(
                    context, getFavoriteImageResource(movie.isFavorite)
                )
                setOnClickListener {
                    onFavoriteMovieClick(movie)
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
            return if (oldItem.isFavorite != newItem.isFavorite) true else null
        }
    }
}

package com.example.topmovies.view


import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import com.example.topmovies.R
import com.example.topmovies.unit.SETTING_PREF_PROFILE_IMAGE

class ImageViewPreference(context: Context, attrs: AttributeSet?) :
    Preference(context, attrs) {
    private lateinit var imageView: ImageView
    private val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        imageView = holder.findViewById(R.id.image) as ImageView
        val stringUriImage = sharedPreference.getString(SETTING_PREF_PROFILE_IMAGE, null)
        if (stringUriImage != null) {
            imageView.setImageURI(stringUriImage.toUri())
        }
    }

    fun setImage(uri: Uri?) {
        imageView.setImageURI(uri)
        saveImagePreference(uri.toString())
    }

    private fun saveImagePreference(uriImage: String) {
        sharedPreference.edit().putString(SETTING_PREF_PROFILE_IMAGE, uriImage).apply()
    }
}
package com.example.topmovies.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.topmovies.R
import com.example.topmovies.unit.SETTING_PREF_DIALOG_ABOUT
import com.example.topmovies.unit.SETTING_PREF_PROFILE_IMAGE
import com.example.topmovies.unit.SETTING_PREF_THEME
import com.example.topmovies.view.ImageViewPreference

class SettingsFragment : PreferenceFragmentCompat() {

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uriImage = result.data?.data
                uriImage?.let {
                    context?.contentResolver?.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                findPreference<ImageViewPreference>(SETTING_PREF_PROFILE_IMAGE)?.setImage(uriImage)
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setTheme(SETTING_PREF_THEME)
        setProfileImage()
        showAbout()
    }

    private fun setTheme(key: String) {
        findPreference<SwitchPreference>(key)?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val switched = newValue as? Boolean ?: false
                if (switched) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
    }

    private fun setProfileImage() {
        val imageViewPreference = findPreference<ImageViewPreference>(SETTING_PREF_PROFILE_IMAGE)
        imageViewPreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            getImageFromPhone()
            true
        }
    }

    private fun getImageFromPhone() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        activityResult.launch(intent)
    }

    private fun showAbout() {
        findPreference<Preference>(SETTING_PREF_DIALOG_ABOUT)?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AboutDialogFragment().show(childFragmentManager,
                    getString(R.string.main_activity_dialog_tag))
                true
            }
    }
}

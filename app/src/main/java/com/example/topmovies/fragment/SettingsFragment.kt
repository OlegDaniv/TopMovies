package com.example.topmovies.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.topmovies.R
import com.example.topmovies.unit.*
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
        setProfileImage()
        showAbout()
        setTheme()
    }
    
    private fun setTheme() {
        findPreference<ListPreference>(SETTING_PREF_THEME)?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                if (preference is ListPreference) {
                    when (newValue) {
                        THEME_MODE_AUTO -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        THEME_MODE_DARK -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        THEME_MODE_LIGHT -> setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
                true
            }
    }
    
    private fun setProfileImage() {
        findPreference<ImageViewPreference>(SETTING_PREF_PROFILE_IMAGE)?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                getImageFromPhone()
                true
            }
    }
    
    private fun getImageFromPhone() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        activityResult.launch(Intent.createChooser(intent, "Select Picture"))
        
    }
    
    private fun showAbout() {
        findPreference<Preference>(SETTING_PREF_DIALOG_ABOUT)?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AboutDialogFragment().show(
                    childFragmentManager, getString(R.string.main_activity_dialog_tag)
                )
                true
            }
    }
}

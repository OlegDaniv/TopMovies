package com.example.topmovies.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.topmovies.R
import com.example.topmovies.presentation.utils.ThemeHandler.Companion.checkNightMode
import com.example.topmovies.presentation.view.ImageViewPreference

class SettingsFragment : PreferenceFragmentCompat() {

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    context?.contentResolver?.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    findPreference<ImageViewPreference>(SETTING_PREF_PROFILE_IMAGE)?.setImage(it)
                }
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
                    checkNightMode(newValue.toString())
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
        intent.type = INTENT_TYPE
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        activityResult.launch(
            Intent.createChooser(
                intent,
                getString(R.string.intent_select_picture)
            )
        )
    }

    private fun showAbout() {
        findPreference<Preference>(SETTING_PREF_DIALOG_ABOUT)?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AboutDialogFragment().show(
                    childFragmentManager,
                    getString(R.string.main_activity_dialog_tag)
                )
                true
            }
    }

    companion object {
        const val SETTING_PREF_THEME = "switch_theme"
        const val SETTING_PREF_PROFILE_IMAGE = "image_preference"
        private const val INTENT_TYPE = "image/*"
        private const val SETTING_PREF_DIALOG_ABOUT = "dialog_preference"
    }
}

package com.example.topmovies.unit

const val BASE_URL = "https://imdb-api.com"
const val TOP_100_MOVIES = "/en/API/MostPopularMovies"
const val MOVIE_DETAILED = "/en/API/Title"
const val DEF_API_KEY = "k_efexam0h"
const val REPLACE_AFTER = "._V1_"
const val IMAGE_SIZE = "UX126_CR6,0,126,126_AL_.jpg"
const val DEF_SIZE_VIEW = 200
const val SHARED_PREF_NAME_FAVORITE = "favorite"
const val RANK_UP = '+'
const val RANK_DOWN = '-'
const val SETTING_PREF_THEME = "switch_theme"
const val SETTING_PREF_PROFILE_IMAGE = "image_preference"
const val SETTING_PREF_DIALOG_ABOUT = "dialog_preference"
const val SETTING_PREF_USER_API_KEY = "api_key_preference"
const val THEME_MODE_AUTO = "Auto"
const val THEME_MODE_LIGHT = "Light"
const val THEME_MODE_DARK = "Dark"
const val INTENT_TYPE  = "image/*"

enum class EnumScreen{
    MOVIES, FAVORITE
}

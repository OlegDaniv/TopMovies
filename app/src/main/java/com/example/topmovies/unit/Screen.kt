package com.example.topmovies.unit

import androidx.annotation.IntDef

const val ALL_MOVIES_SCREEN = 0
const val FAVOURITE_MOVIES_SCREEN = 1

@IntDef(FAVOURITE_MOVIES_SCREEN, ALL_MOVIES_SCREEN)
@Retention(AnnotationRetention.SOURCE)
annotation class Screen

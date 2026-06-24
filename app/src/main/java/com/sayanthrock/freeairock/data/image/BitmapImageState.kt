package com.sayanthrock.freeairock.data.image

import android.graphics.Bitmap

sealed interface BitmapImageState {
    data object Idle : BitmapImageState
    data object Loading : BitmapImageState
    data class Success(val image: Bitmap) : BitmapImageState
    data class Error(val message: String) : BitmapImageState
}

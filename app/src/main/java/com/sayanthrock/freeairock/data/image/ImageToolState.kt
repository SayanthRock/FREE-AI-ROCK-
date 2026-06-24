package com.sayanthrock.freeairock.data.image

sealed interface ImageToolState {
    data object Idle : ImageToolState
    data object Loading : ImageToolState
    data class Ready(val previewUrl: String, val sourcePrompt: String) : ImageToolState
    data class Failure(val reason: String) : ImageToolState
}

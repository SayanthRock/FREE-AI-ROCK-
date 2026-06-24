package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import com.sayanthrock.freeairock.data.image.BitmapImageState
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageViewModel(
    private val secureStorage: SecureStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<BitmapImageState>(BitmapImageState.Idle)
    val uiState: StateFlow<BitmapImageState> = _uiState.asStateFlow()

    fun refreshRenderer() {
        secureStorage.getGeminiKey()
    }

    fun create(prompt: String) {
        if (prompt.isBlank()) {
            _uiState.value = BitmapImageState.Error("Prompt cannot be empty")
            return
        }

        _uiState.value = BitmapImageState.Error(
            "Image Studio UI is not enabled in this build yet."
        )
    }

    fun reset() {
        _uiState.value = BitmapImageState.Idle
    }
}

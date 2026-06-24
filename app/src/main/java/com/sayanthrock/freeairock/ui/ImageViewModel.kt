package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayanthrock.freeairock.data.image.AiImageRenderer
import com.sayanthrock.freeairock.data.image.BitmapImageState
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageViewModel(
    private val secureStorage: SecureStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<BitmapImageState>(BitmapImageState.Idle)
    val uiState: StateFlow<BitmapImageState> = _uiState.asStateFlow()

    private var renderer: AiImageRenderer? = secureStorage.getGeminiKey()?.let(::AiImageRenderer)

    fun refreshRenderer() {
        renderer = secureStorage.getGeminiKey()?.let(::AiImageRenderer)
    }

    fun create(prompt: String) {
        if (prompt.isBlank()) {
            _uiState.value = BitmapImageState.Error("Prompt cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = BitmapImageState.Loading

            val activeRenderer = renderer
            if (activeRenderer == null) {
                _uiState.value = BitmapImageState.Error("Gemini key missing. Add it in setup first.")
                return@launch
            }

            activeRenderer.render(prompt)
                .onSuccess { bitmap ->
                    _uiState.value = BitmapImageState.Success(bitmap)
                }
                .onFailure { error ->
                    _uiState.value = BitmapImageState.Error(
                        error.localizedMessage ?: "Image request failed"
                    )
                }
        }
    }

    fun reset() {
        _uiState.value = BitmapImageState.Idle
    }
}

package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayanthrock.freeairock.data.image.ImageRepository
import com.sayanthrock.freeairock.data.image.ImageToolState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageViewModel(
    private val repository: ImageRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ImageToolState>(ImageToolState.Idle)
    val state: StateFlow<ImageToolState> = _state.asStateFlow()

    fun submit(prompt: String) {
        viewModelScope.launch {
            _state.value = ImageToolState.Loading

            val result = repository.requestPreview(prompt)
            _state.value = result.fold(
                onSuccess = { url -> ImageToolState.Ready(url, prompt) },
                onFailure = { error -> ImageToolState.Failure(error.localizedMessage ?: "Request failed") }
            )
        }
    }

    fun reset() {
        _state.value = ImageToolState.Idle
    }
}

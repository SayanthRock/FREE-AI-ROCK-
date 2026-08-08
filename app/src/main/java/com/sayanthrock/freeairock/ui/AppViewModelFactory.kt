package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayanthrock.freeairock.data.storage.SecureStorageManager

class AppViewModelFactory(
    private val secureStorage: SecureStorageManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ImageViewModel::class.java) -> {
                ImageViewModel(secureStorage) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

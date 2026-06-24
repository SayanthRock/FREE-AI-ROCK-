package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager

class AppViewModelFactory(
    private val secureStorage: SecureStorageManager,
    private val apiService: GitHubApiService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                ReviewViewModel(secureStorage, apiService) as T
            }

            modelClass.isAssignableFrom(AppViewModel::class.java) -> {
                AppViewModel(secureStorage, apiService) as T
            }

            modelClass.isAssignableFrom(ImageViewModel::class.java) -> {
                ImageViewModel(secureStorage) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

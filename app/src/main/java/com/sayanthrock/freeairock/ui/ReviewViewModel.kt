package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayanthrock.freeairock.data.ai.AiCodeAnalyzer
import com.sayanthrock.freeairock.data.ai.CodeAnalysisState
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val storage: SecureStorageManager,
    private val service: GitHubApiService
) : ViewModel() {

    private val _state = MutableStateFlow<CodeAnalysisState>(CodeAnalysisState.Idle)
    val state: StateFlow<CodeAnalysisState> = _state.asStateFlow()

    fun run(owner: String, repo: String, numberText: String) {
        viewModelScope.launch {
            _state.value = CodeAnalysisState.Loading
            try {
                val token = storage.getGeminiKey() ?: error("Gemini key missing")
                val number = numberText.trim().toIntOrNull() ?: error("Invalid number")
                val diffText = service.getPullRequestDiff(owner.trim(), repo.trim(), number).string()
                val result = AiCodeAnalyzer(token).summarizePullRequest(owner.trim(), repo.trim(), number, diffText)
                _state.value = CodeAnalysisState.Success(result)
            } catch (error: Exception) {
                _state.value = CodeAnalysisState.Error(error.localizedMessage ?: "Review failed")
            }
        }
    }
}

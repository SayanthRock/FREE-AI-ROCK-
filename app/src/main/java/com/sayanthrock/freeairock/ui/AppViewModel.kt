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

class AppViewModel(
    private val secureStorage: SecureStorageManager,
    private val githubApiService: GitHubApiService
) : ViewModel() {

    private val _analysisState = MutableStateFlow<CodeAnalysisState>(CodeAnalysisState.Idle)
    val analysisState: StateFlow<CodeAnalysisState> = _analysisState.asStateFlow()

    private var aiAnalyzer: AiCodeAnalyzer? = secureStorage.getGeminiKey()?.let(::AiCodeAnalyzer)

    fun refreshAiAnalyzer() {
        aiAnalyzer = secureStorage.getGeminiKey()?.let(::AiCodeAnalyzer)
    }

    fun saveKeys(githubToken: String, geminiKey: String) {
        secureStorage.saveGitHubToken(githubToken)
        secureStorage.saveGeminiKey(geminiKey)
        refreshAiAnalyzer()
    }

    fun analyzeCodeFile(fileName: String, downloadUrl: String?) {
        viewModelScope.launch {
            _analysisState.value = CodeAnalysisState.Loading

            try {
                val rawUrl = downloadUrl?.takeIf { it.isNotBlank() }
                    ?: error("Raw file download URL missing")

                val analyzer = aiAnalyzer ?: error("Gemini key missing. Add it in settings first.")
                val rawCode = githubApiService.downloadRawFile(rawUrl).string()
                val result = analyzer.analyzeCode(fileName, rawCode)

                _analysisState.value = CodeAnalysisState.Success(result)
            } catch (error: Exception) {
                _analysisState.value = CodeAnalysisState.Error(
                    error.localizedMessage ?: "Unknown analysis error"
                )
            }
        }
    }

    fun resetAnalysis() {
        _analysisState.value = CodeAnalysisState.Idle
    }
}

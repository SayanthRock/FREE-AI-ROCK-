package com.sayanthrock.freeairock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sayanthrock.freeairock.data.ai.AiProvider
import com.sayanthrock.freeairock.data.ai.CodeAnalysisState
import com.sayanthrock.freeairock.data.chat.ChatMessage
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import com.sayanthrock.freeairock.data.ai.PollinationsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val secureStorage: SecureStorageManager,
    private val githubApiService: GitHubApiService,
    private val pollinationsApiService: PollinationsApiService,
    private val aiProvider: AiProvider? = null // Optional for now
) : ViewModel() {

    private val _analysisState = MutableStateFlow<CodeAnalysisState>(CodeAnalysisState.Idle)
    val analysisState: StateFlow<CodeAnalysisState> = _analysisState.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun sendMessage(content: String) {
        val userMsg = ChatMessage(conversationId = 1, role = "user", content = content)
        val aiMsg = ChatMessage(conversationId = 1, role = "model", content = "Generating...") // Placeholder

        _chatMessages.value = _chatMessages.value + listOf(userMsg, aiMsg)

        viewModelScope.launch {
            try {
                // Temporary fake response for demo purposes
                val response = "This is a response from FREE AI ROCK for: $content"
                _chatMessages.value = _chatMessages.value.dropLast(1) + aiMsg.copy(content = response)
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value.dropLast(1) + aiMsg.copy(content = "Error generating response.")
            }
        }
    }

    fun saveKeys(githubToken: String) {
        secureStorage.saveGitHubToken(githubToken)
    }

    fun analyzeCodeFile(fileName: String, downloadUrl: String?) {
        _analysisState.value = CodeAnalysisState.Loading
        viewModelScope.launch {
            try {
                _analysisState.value = CodeAnalysisState.Success("Analyzed $fileName")
            } catch (e: Exception) {
                _analysisState.value = CodeAnalysisState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetAnalysis() {
        _analysisState.value = CodeAnalysisState.Idle
    }
}

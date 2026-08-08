package com.sayanthrock.freeairock.data.ai

sealed interface CodeAnalysisState {
    data object Idle : CodeAnalysisState
    data object Loading : CodeAnalysisState
    data class Success(val result: String) : CodeAnalysisState
    data class Error(val message: String) : CodeAnalysisState
}

package com.sayanthrock.freeairock.data.ai

import kotlinx.coroutines.flow.Flow

interface AiProvider {
    suspend fun generateResponse(prompt: String): String
    fun streamResponse(prompt: String): Flow<String>
}

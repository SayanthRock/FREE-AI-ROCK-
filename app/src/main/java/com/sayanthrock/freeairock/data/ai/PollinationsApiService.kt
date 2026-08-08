package com.sayanthrock.freeairock.data.ai

import retrofit2.http.Body
import retrofit2.http.POST

data class PollinationsMessage(
    val role: String,
    val content: String
)

data class PollinationsRequest(
    val messages: List<PollinationsMessage>,
    val model: String? = null
)

interface PollinationsApiService {
    @POST("/")
    suspend fun generateText(
        @Body request: PollinationsRequest
    ): String
}

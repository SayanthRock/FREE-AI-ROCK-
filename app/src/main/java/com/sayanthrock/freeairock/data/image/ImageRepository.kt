package com.sayanthrock.freeairock.data.image

class ImageRepository(
    private val api: ImageToolApi
) {
    suspend fun requestPreview(prompt: String): Result<String> {
        return runCatching {
            require(prompt.isNotBlank()) { "Prompt is required" }
            val response = api.createImage(ImageToolRequest(prompt = prompt.trim()))
            response.firstPreviewUrl() ?: error("No preview URL returned")
        }
    }
}

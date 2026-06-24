package com.sayanthrock.freeairock.data.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiImageRenderer(
    apiKey: String,
    modelName: String = DEFAULT_MODEL_NAME
) {
    private val model = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    suspend fun render(prompt: String): Result<Bitmap> = withContext(Dispatchers.IO) {
        runCatching {
            require(prompt.isNotBlank()) { "Prompt cannot be empty" }

            val response = model.generateContent(
                content { text(prompt.trim()) }
            )

            val rawPayload = response.text?.trim().orEmpty()
            require(rawPayload.isNotBlank()) { "Model returned empty image payload" }

            val base64Payload = rawPayload
                .substringAfter("base64,", rawPayload)
                .replace("```", "")
                .replace("\n", "")
                .trim()

            val imageBytes = Base64.decode(base64Payload, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: error("Unable to decode image payload")
        }
    }

    companion object {
        const val DEFAULT_MODEL_NAME = "gemini-3.1-flash-image"
    }
}

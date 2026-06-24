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

            val structuredBytes = ImagePayloadExtractor.findImageBytes(response)
            val imageBytes = structuredBytes ?: extractLegacyTextPayload(response.text)

            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: error("Unable to decode image payload")
        }
    }

    private fun extractLegacyTextPayload(textPayload: String?): ByteArray {
        val rawPayload = textPayload?.trim().orEmpty()
        require(rawPayload.isNotBlank()) {
            "No image data found in structured response parts or text fallback"
        }

        val base64Payload = rawPayload
            .substringAfter("base64,", rawPayload)
            .replace("```", "")
            .replace("\n", "")
            .trim()

        return Base64.decode(base64Payload, Base64.DEFAULT)
    }

    companion object {
        const val DEFAULT_MODEL_NAME = "gemini-3.1-flash-image"
    }
}

private object ImagePayloadExtractor {

    fun findImageBytes(response: Any): ByteArray? {
        return scan(response, mutableSetOf())
    }

    private fun scan(value: Any?, visited: MutableSet<Int>): ByteArray? {
        if (value == null) return null

        val identity = System.identityHashCode(value)
        if (!visited.add(identity)) return null

        when (value) {
            is ByteArray -> return value.takeIf { it.isNotEmpty() }
            is Iterable<*> -> value.forEach { item -> scan(item, visited)?.let { return it } }
            is Array<*> -> value.forEach { item -> scan(item, visited)?.let { return it } }
            is Map<*, *> -> value.values.forEach { item -> scan(item, visited)?.let { return it } }
        }

        val javaClass = value.javaClass
        if (javaClass.name.startsWith("java.") || javaClass.name.startsWith("kotlin.")) {
            return null
        }

        javaClass.declaredFields.forEach { field ->
            runCatching {
                field.isAccessible = true
                val fieldValue = field.get(value)
                if (fieldValue is ByteArray && looksLikeImageContainer(value)) {
                    return fieldValue.takeIf { it.isNotEmpty() }
                }
                scan(fieldValue, visited)?.let { return it }
            }
        }

        return null
    }

    private fun looksLikeImageContainer(value: Any): Boolean {
        val debugText = value.toString().lowercase()
        return debugText.contains("image/") ||
            debugText.contains("png") ||
            debugText.contains("jpeg") ||
            debugText.contains("jpg") ||
            value.javaClass.name.lowercase().contains("blob") ||
            value.javaClass.name.lowercase().contains("inline")
    }
}

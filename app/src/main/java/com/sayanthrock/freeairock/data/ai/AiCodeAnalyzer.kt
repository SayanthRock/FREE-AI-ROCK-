package com.sayanthrock.freeairock.data.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class AiCodeAnalyzer(apiKey: String) {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun analyzeCode(fileName: String, rawCode: String): String {
        val safeFileName = fileName.ifBlank { "selected-file" }
        val prompt = buildString {
            appendLine("You are an expert software developer.")
            appendLine("Analyze the file named '$safeFileName'.")
            appendLine("Explain its core functionality, architecture role, important methods, risks, and improvement ideas.")
            appendLine("Keep the explanation clear for a junior developer.")
            appendLine()
            appendLine("Code:")
            appendLine("```")
            appendLine(rawCode.take(MAX_CODE_CHARS))
            appendLine("```")
        }

        return try {
            val response = model.generateContent(
                content { text(prompt) }
            )
            response.text ?: "No explanation generated."
        } catch (error: Exception) {
            "AI analysis failed: ${error.localizedMessage ?: "Unknown error"}"
        }
    }

    companion object {
        private const val MAX_CODE_CHARS = 12000
    }
}

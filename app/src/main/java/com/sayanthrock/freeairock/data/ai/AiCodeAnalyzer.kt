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

        return generateText(prompt, "No explanation generated.")
    }

    suspend fun summarizePullRequest(owner: String, repo: String, pullNumber: Int, diffText: String): String {
        val safeOwner = owner.ifBlank { "owner" }
        val safeRepo = repo.ifBlank { "repo" }
        val prompt = buildString {
            appendLine("You are a senior code reviewer.")
            appendLine("Summarize pull request #$pullNumber in $safeOwner/$safeRepo using the Git diff below.")
            appendLine("Focus on developer impact, affected files, behavior changes, risks, testing notes, and release notes.")
            appendLine("Use clear plain English. Avoid repeating every line of the diff.")
            appendLine()
            appendLine("Return this structure:")
            appendLine("1. Summary")
            appendLine("2. Key changes")
            appendLine("3. Risk level")
            appendLine("4. Testing checklist")
            appendLine("5. Suggested release note")
            appendLine()
            appendLine("Git diff:")
            appendLine("```diff")
            appendLine(diffText.take(MAX_DIFF_CHARS))
            appendLine("```")
        }

        return generateText(prompt, "No pull request summary generated.")
    }

    private suspend fun generateText(prompt: String, fallback: String): String {
        return try {
            val response = model.generateContent(
                content { text(prompt) }
            )
            response.text ?: fallback
        } catch (error: Exception) {
            "AI analysis failed: ${error.localizedMessage ?: "Unknown error"}"
        }
    }

    companion object {
        private const val MAX_CODE_CHARS = 12000
        private const val MAX_DIFF_CHARS = 18000
    }
}

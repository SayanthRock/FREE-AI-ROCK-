package com.sayanthrock.freeairock.data.storage

import android.content.Context

class SecureStorageManager(context: Context? = null) {
    private val prefs = context?.applicationContext?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val memory = mutableMapOf<String, String>()

    fun saveGitHubToken(token: String) {
        saveString(KEY_GITHUB_TOKEN, token.trim())
    }

    fun getGitHubToken(): String? {
        return readString(KEY_GITHUB_TOKEN)?.takeIf { it.isNotBlank() }
    }

    fun saveGeminiKey(key: String) {
        saveString(KEY_GEMINI_KEY, key.trim())
    }

    fun getGeminiKey(): String? {
        return readString(KEY_GEMINI_KEY)?.takeIf { it.isNotBlank() }
    }

    fun clearSecrets() {
        prefs?.edit()?.clear()?.apply()
        memory.clear()
    }

    private fun saveString(name: String, value: String) {
        prefs?.edit()?.putString(name, value)?.apply() ?: memory.set(name, value)
    }

    private fun readString(name: String): String? {
        return prefs?.getString(name, null) ?: memory[name]
    }

    companion object {
        private const val PREF_NAME = "free_ai_rock_prefs"
        private const val KEY_GITHUB_TOKEN = "github_token"
        private const val KEY_GEMINI_KEY = "gemini_key"
    }
}

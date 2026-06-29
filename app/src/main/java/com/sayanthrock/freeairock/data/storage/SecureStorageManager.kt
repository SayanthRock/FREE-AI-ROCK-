package com.sayanthrock.freeairock.data.storage

import android.content.Context

interface AppKeyStore {
    fun saveGitHubToken(token: String)
    fun getGitHubToken(): String?
    fun saveGeminiKey(key: String)
    fun getGeminiKey(): String?
    fun clearSecrets()
}

class SecureStorageManager(context: Context) : AppKeyStore {
    private val prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun saveGitHubToken(token: String) {
        prefs.edit().putString(KEY_GITHUB_TOKEN, token.trim()).apply()
    }

    override fun getGitHubToken(): String? {
        return prefs.getString(KEY_GITHUB_TOKEN, null)?.takeIf { it.isNotBlank() }
    }

    override fun saveGeminiKey(key: String) {
        prefs.edit().putString(KEY_GEMINI_KEY, key.trim()).apply()
    }

    override fun getGeminiKey(): String? {
        return prefs.getString(KEY_GEMINI_KEY, null)?.takeIf { it.isNotBlank() }
    }

    override fun clearSecrets() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "free_ai_rock_prefs"
        private const val KEY_GITHUB_TOKEN = "github_token"
        private const val KEY_GEMINI_KEY = "gemini_key"
    }
}

package com.sayanthrock.freeairock.data.storage

import android.content.Context

class SecureStorageManager(context: Context) {

    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveGitHubToken(token: String) {
        prefs.edit().putString(KEY_GITHUB_TOKEN, token.trim()).apply()
    }

    fun getGitHubToken(): String? {
        return prefs.getString(KEY_GITHUB_TOKEN, null)?.takeIf { it.isNotBlank() }
    }

    fun saveGeminiKey(key: String) {
        prefs.edit().putString(KEY_GEMINI_KEY, key.trim()).apply()
    }

    fun getGeminiKey(): String? {
        return prefs.getString(KEY_GEMINI_KEY, null)?.takeIf { it.isNotBlank() }
    }

    fun clearSecrets() {
        prefs.edit()
            .remove(KEY_GITHUB_TOKEN)
            .remove(KEY_GEMINI_KEY)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "free_ai_rock_prefs"
        private const val KEY_GITHUB_TOKEN = "github_token"
        private const val KEY_GEMINI_KEY = "gemini_key"
    }
}

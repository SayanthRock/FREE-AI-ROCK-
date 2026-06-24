package com.sayanthrock.freeairock.data.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorageManager(context: Context) {

    private val appContext = context.applicationContext

    private val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePrefs = EncryptedSharedPreferences.create(
        appContext,
        PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveGitHubToken(token: String) {
        securePrefs.edit().putString(KEY_GITHUB_TOKEN, token.trim()).apply()
    }

    fun getGitHubToken(): String? {
        return securePrefs.getString(KEY_GITHUB_TOKEN, null)?.takeIf { it.isNotBlank() }
    }

    fun saveGeminiKey(key: String) {
        securePrefs.edit().putString(KEY_GEMINI_KEY, key.trim()).apply()
    }

    fun getGeminiKey(): String? {
        return securePrefs.getString(KEY_GEMINI_KEY, null)?.takeIf { it.isNotBlank() }
    }

    fun clearSecrets() {
        securePrefs.edit()
            .remove(KEY_GITHUB_TOKEN)
            .remove(KEY_GEMINI_KEY)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "free_ai_rock_secure_prefs"
        private const val KEY_GITHUB_TOKEN = "github_token"
        private const val KEY_GEMINI_KEY = "gemini_key"
    }
}

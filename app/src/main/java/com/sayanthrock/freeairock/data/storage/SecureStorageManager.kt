package com.sayanthrock.freeairock.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorageManager(context: Context? = null) {
    private val appContext = context?.applicationContext
    private val memory = mutableMapOf<String, String>()
    private val legacyPrefs: SharedPreferences? = appContext?.getSharedPreferences(
        FALLBACK_PREF_NAME,
        Context.MODE_PRIVATE
    )
    private val prefs: SharedPreferences? = appContext?.let(::createPreferences)

    init {
        migrateLegacySecrets()
    }

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
        legacyPrefs?.edit()?.clear()?.apply()
        memory.clear()
    }

    private fun saveString(name: String, value: String) {
        if (value.isBlank()) {
            prefs?.edit()?.remove(name)?.apply()
            legacyPrefs?.edit()?.remove(name)?.apply()
            memory.remove(name)
            return
        }

        prefs?.edit()?.putString(name, value)?.apply() ?: memory.set(name, value)
        legacyPrefs?.edit()?.remove(name)?.apply()
    }

    private fun readString(name: String): String? {
        return prefs?.getString(name, null)
            ?: legacyPrefs?.getString(name, null)
            ?: memory[name]
    }

    private fun migrateLegacySecrets() {
        val securePrefs = prefs ?: return
        val oldPrefs = legacyPrefs ?: return

        listOf(KEY_GITHUB_TOKEN, KEY_GEMINI_KEY).forEach { key ->
            val legacyValue = oldPrefs.getString(key, null)
            if (!legacyValue.isNullOrBlank() && securePrefs.getString(key, null).isNullOrBlank()) {
                securePrefs.edit().putString(key, legacyValue).apply()
            }
        }

        oldPrefs.edit().clear().apply()
    }

    @Suppress("DEPRECATION")
    private fun createPreferences(context: Context): SharedPreferences {
        return runCatching {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }.getOrElse {
            context.getSharedPreferences(FALLBACK_PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    companion object {
        private const val PREF_NAME = "free_ai_rock_secure_prefs"
        private const val FALLBACK_PREF_NAME = "free_ai_rock_prefs"
        private const val KEY_GITHUB_TOKEN = "github_token"
        private const val KEY_GEMINI_KEY = "gemini_key"
    }
}

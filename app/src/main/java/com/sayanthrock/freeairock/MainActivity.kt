package com.sayanthrock.freeairock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import com.sayanthrock.freeairock.ui.AppViewModel
import com.sayanthrock.freeairock.ui.AppViewModelFactory
import com.sayanthrock.freeairock.ui.ThemeMode
import com.sayanthrock.freeairock.ui.chat.ChatScreen
import com.sayanthrock.freeairock.ui.theme.FreeAiRockTheme
import com.sayanthrock.freeairock.data.ai.PollinationsApiService
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val secureStorage by lazy { SecureStorageManager(this) }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = secureStorage.getGitHubToken()

                if (!token.isNullOrBlank()) {
                    requestBuilder.header("Authorization", "Bearer \$token")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    private val githubApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }

    private val pollinationsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://text.pollinations.ai/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PollinationsApiService::class.java)
    }

    private val viewModelFactory by lazy {
        AppViewModelFactory(secureStorage, githubApiService, pollinationsApiService)
    }

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val appViewModel: AppViewModel by lazy {
        viewModelProvider[AppViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var appTheme by remember { mutableStateOf(ThemeMode.SYSTEM) }
            val darkTheme = when (appTheme) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            FreeAiRockTheme(darkTheme = darkTheme) {
                ChatScreen(viewModel = appViewModel)
            }
        }
    }
}

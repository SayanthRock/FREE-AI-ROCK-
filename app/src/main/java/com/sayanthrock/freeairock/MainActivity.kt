package com.sayanthrock.freeairock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import com.sayanthrock.freeairock.ui.AboutScreen
import com.sayanthrock.freeairock.ui.AppViewModel
import com.sayanthrock.freeairock.ui.AppViewModelFactory
import com.sayanthrock.freeairock.ui.HomeScaffold
import com.sayanthrock.freeairock.ui.ImageViewModel
import com.sayanthrock.freeairock.ui.PlaceholderPanel
import com.sayanthrock.freeairock.ui.ReviewScreen
import com.sayanthrock.freeairock.ui.ReviewViewModel
import com.sayanthrock.freeairock.ui.ThemeMode
import com.sayanthrock.freeairock.ui.theme.FreeAiRockTheme
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
                    requestBuilder.header("Authorization", "Bearer $token")
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

    private val viewModelFactory by lazy {
        AppViewModelFactory(secureStorage, githubApiService)
    }

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val appViewModel: AppViewModel by lazy {
        viewModelProvider[AppViewModel::class.java]
    }

    private val reviewViewModel: ReviewViewModel by lazy {
        viewModelProvider[ReviewViewModel::class.java]
    }

    private val imageViewModel: ImageViewModel by lazy {
        viewModelProvider[ImageViewModel::class.java]
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
                HomeScaffold(
                    codeContent = { modifier ->
                        SetupScreen(
                            onSave = { githubToken, geminiKey ->
                                appViewModel.saveKeys(githubToken, geminiKey)
                                imageViewModel.refreshRenderer()
                            },
                            modifier = modifier
                        )
                    },
                    reviewContent = { modifier ->
                        ReviewScreen(
                            viewModel = reviewViewModel,
                            modifier = modifier
                        )
                    },
                    studioContent = { modifier ->
                        PlaceholderPanel(
                            title = "Image Studio",
                            body = "Image renderer, bitmap state, and gallery save helper are ready. Full creation UI will connect here next.",
                            modifier = modifier
                        )
                    },
                    aboutContent = { modifier ->
                        AboutScreen(
                            currentTheme = appTheme,
                            onThemeChange = { appTheme = it },
                            modifier = modifier
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SetupScreen(
    onSave: (githubToken: String, geminiKey: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var githubToken by remember { mutableStateOf("") }
    var geminiKey by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "FREE-AI-ROCK",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Secure setup for GitHub analysis and AI code summaries.",
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = githubToken,
                onValueChange = { githubToken = it },
                label = { Text("GitHub token") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = geminiKey,
                onValueChange = { geminiKey = it },
                label = { Text("Gemini key") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSave(githubToken, geminiKey)
                    savedMessage = "Saved securely on this device"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Securely")
            }

            savedMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

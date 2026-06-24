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
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import com.sayanthrock.freeairock.ui.AboutScreen
import com.sayanthrock.freeairock.ui.HomeScaffold
import com.sayanthrock.freeairock.ui.PlaceholderPanel
import com.sayanthrock.freeairock.ui.ReviewScreen
import com.sayanthrock.freeairock.ui.ReviewViewModel
import com.sayanthrock.freeairock.ui.ThemeMode
import com.sayanthrock.freeairock.ui.theme.FreeAiRockTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val secureStorage = SecureStorageManager(this)
        val githubApiService = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
        val reviewViewModel = ReviewViewModel(secureStorage, githubApiService)

        setContent {
            var appTheme by remember { mutableStateOf(ThemeMode.SYSTEM) }
            val darkTheme = when (appTheme) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            FreeAiRockTheme(darkTheme = darkTheme) {
                HomeScaffold(
                    codeContent = {
                        SetupScreen(
                            onSave = { githubToken, geminiKey ->
                                secureStorage.saveGitHubToken(githubToken)
                                secureStorage.saveGeminiKey(geminiKey)
                            }
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
    onSave: (githubToken: String, geminiKey: String) -> Unit
) {
    var githubToken by remember { mutableStateOf("") }
    var geminiKey by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
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

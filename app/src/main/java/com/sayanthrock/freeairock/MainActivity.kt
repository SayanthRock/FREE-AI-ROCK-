package com.sayanthrock.freeairock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.sayanthrock.freeairock.data.ai.CodeAnalysisState
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
                        CodeAnalyzerScreen(
                            uiState = appViewModel.analysisState.collectAsState().value,
                            onSave = { githubToken, geminiKey ->
                                appViewModel.saveKeys(githubToken, geminiKey)
                                imageViewModel.refreshRenderer()
                            },
                            onAnalyze = appViewModel::analyzeCodeFile,
                            onReset = appViewModel::resetAnalysis,
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
private fun CodeAnalyzerScreen(
    uiState: CodeAnalysisState,
    onSave: (githubToken: String, geminiKey: String) -> Unit,
    onAnalyze: (fileName: String, downloadUrl: String?) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var githubToken by remember { mutableStateOf("") }
    var geminiKey by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("MainActivity.kt") }
    var fileUrl by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "FREE-AI-ROCK",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Secure setup for GitHub file analysis and AI code summaries.",
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

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Code AI",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fileName,
                onValueChange = { fileName = it },
                label = { Text("File name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fileUrl,
                onValueChange = { fileUrl = it },
                label = { Text("GitHub raw/blob file URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAnalyze(fileName, fileUrl) },
                enabled = uiState !is CodeAnalysisState.Loading && fileUrl.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState is CodeAnalysisState.Loading) "Analyzing..." else "Summarize Code with AI")
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {
                CodeAnalysisState.Idle -> Text(
                    text = "Paste a GitHub raw URL or normal blob URL, then run the analyzer.",
                    color = MaterialTheme.colorScheme.onBackground
                )

                CodeAnalysisState.Loading -> CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )

                is CodeAnalysisState.Success -> {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.result,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { clipboardManager.setText(AnnotatedString(uiState.result)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Copy Result")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onReset,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reset")
                    }
                }

                is CodeAnalysisState.Error -> Text(
                    text = "Error: ${uiState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

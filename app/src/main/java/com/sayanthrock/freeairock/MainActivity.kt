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
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import com.sayanthrock.freeairock.ui.AboutScreen
import com.sayanthrock.freeairock.ui.AppViewModelFactory
import com.sayanthrock.freeairock.ui.HomeScaffold
import com.sayanthrock.freeairock.ui.ImageStudioScreen
import com.sayanthrock.freeairock.ui.ImageViewModel
import com.sayanthrock.freeairock.ui.ThemeMode
import com.sayanthrock.freeairock.ui.theme.FreeAiRockTheme

class MainActivity : ComponentActivity() {

    private val secureStorage by lazy { SecureStorageManager(this) }

    private val viewModelFactory by lazy {
        AppViewModelFactory(secureStorage)
    }

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
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
                    studioContent = { modifier ->
                        ImageStudioScreen(modifier = modifier)
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

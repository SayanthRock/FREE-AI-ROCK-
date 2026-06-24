package com.sayanthrock.freeairock.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Composable
fun AboutScreen(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Settings & About",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Theme",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "UI Design",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val themeOptions = listOf(ThemeMode.SYSTEM, ThemeMode.LIGHT, ThemeMode.DARK)
        val themeLabels = listOf("System Auto Color", "Light Mode", "Dark Mode")

        themeOptions.forEachIndexed { index, themeMode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeChange(themeMode) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = currentTheme == themeMode,
                    onClick = { onThemeChange(themeMode) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = themeLabels[index],
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Developer",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contact",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "FREE-AI-ROCK",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = "Designed and developed by @SayanthRock in Kerala, India.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { uriHandler.openUri("https://github.com/SayanthRock") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Filled.Home, contentDescription = "GitHub")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Explore on GitHub", fontFamily = FontFamily.Monospace)
        }
    }
}

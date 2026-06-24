package com.sayanthrock.freeairock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object CodeAnalyzer : Screen(
        route = "code_analyzer",
        title = "Code AI",
        icon = Icons.Filled.Home
    )

    data object PullRequestReview : Screen(
        route = "pr_review",
        title = "PR Review",
        icon = Icons.Filled.List
    )

    data object ImageStudio : Screen(
        route = "image_studio",
        title = "Image Studio",
        icon = Icons.Filled.Settings
    )

    data object About : Screen(
        route = "about",
        title = "Settings",
        icon = Icons.Filled.Person
    )
}

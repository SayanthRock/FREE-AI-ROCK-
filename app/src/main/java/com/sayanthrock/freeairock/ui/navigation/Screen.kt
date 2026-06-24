package com.sayanthrock.freeairock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object CodeAnalyzer : Screen(
        route = "code_analyzer",
        title = "Code AI",
        icon = Icons.Filled.Code
    )

    data object PullRequestReview : Screen(
        route = "pr_review",
        title = "PR Review",
        icon = Icons.Filled.RateReview
    )

    data object ImageStudio : Screen(
        route = "image_studio",
        title = "Image Studio",
        icon = Icons.Filled.Brush
    )
}

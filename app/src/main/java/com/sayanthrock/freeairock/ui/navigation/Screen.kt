package com.sayanthrock.freeairock.ui.navigation

sealed class Screen(
    val route: String,
    val title: String,
    val shortLabel: String
) {
    data object CodeAnalyzer : Screen(
        route = "code_analyzer",
        title = "Code AI",
        shortLabel = "AI"
    )

    data object PullRequestReview : Screen(
        route = "pr_review",
        title = "PR Review",
        shortLabel = "PR"
    )

    data object ImageStudio : Screen(
        route = "image_studio",
        title = "Image Studio",
        shortLabel = "IMG"
    )

    data object About : Screen(
        route = "about",
        title = "Settings",
        shortLabel = "SET"
    )
}

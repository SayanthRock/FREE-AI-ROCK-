package com.sayanthrock.freeairock.ui.navigation

sealed class Screen(
    val route: String,
    val title: String,
    val shortLabel: String
) {
    data object ImageStudio : Screen(
        route = "image_studio",
        title = "Image Studio",
        shortLabel = "◩"
    )

    data object About : Screen(
        route = "about",
        title = "Settings",
        shortLabel = "⚙"
    )
}

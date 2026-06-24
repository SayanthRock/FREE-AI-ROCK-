package com.sayanthrock.freeairock.data.image

import com.google.gson.annotations.SerializedName

data class ImageToolRequest(
    val prompt: String,
    val size: String = "1024x1024",
    val quality: String = "standard"
)

data class ImageToolResponse(
    @SerializedName("image_url") val imageUrl: String? = null,
    val url: String? = null,
    val data: List<ImageToolData>? = null
) {
    fun firstPreviewUrl(): String? {
        return imageUrl ?: url ?: data?.firstOrNull()?.url
    }
}

data class ImageToolData(
    val url: String? = null
)

package com.sayanthrock.freeairock.data.image

import retrofit2.http.Body
import retrofit2.http.POST

interface ImageToolApi {
    @POST("images")
    suspend fun createImage(
        @Body request: ImageToolRequest
    ): ImageToolResponse
}

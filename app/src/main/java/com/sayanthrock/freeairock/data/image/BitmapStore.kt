package com.sayanthrock.freeairock.data.image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore

object BitmapStore {

    fun writePng(context: Context, bitmap: Bitmap): Result<String> {
        return runCatching {
            val name = "FREE_AI_ROCK_${System.currentTimeMillis()}.png"
            val resolver = context.applicationContext.contentResolver

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/FREE-AI-ROCK")
            }

            val target = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: error("Unable to create media item")

            resolver.openOutputStream(target)?.use { stream ->
                require(bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                    "Unable to encode PNG"
                }
            } ?: error("Unable to open output stream")

            "Saved to Pictures/FREE-AI-ROCK/$name"
        }
    }
}

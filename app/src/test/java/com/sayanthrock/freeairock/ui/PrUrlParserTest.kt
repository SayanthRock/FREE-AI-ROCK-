package com.sayanthrock.freeairock.ui

import com.sayanthrock.freeairock.extractFileNameFromUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PrUrlParserTest {

    @Test
    fun testParseGitHubPrUrl_validUrls() {
        val testCases = listOf(
            "https://github.com/SayanthRock/FREE-AI-ROCK-/pull/12" to Triple("SayanthRock", "FREE-AI-ROCK-", "12"),
            "http://github.com/SayanthRock/Root-apk/pull/123" to Triple("SayanthRock", "Root-apk", "123"),
            "github.com/SayanthRock/Root-apk/pull/123/" to Triple("SayanthRock", "Root-apk", "123"),
            "https://www.github.com/SayanthRock/Root-apk/pull/123/files" to Triple("SayanthRock", "Root-apk", "123"),
            "https://github.com/SayanthRock/Root-apk/pull/123?diff=unified" to Triple("SayanthRock", "Root-apk", "123")
        )

        for ((input, expected) in testCases) {
            val result = parseGitHubPrUrl(input)
            assertEquals("Failed on input: $input", expected, result)
        }
    }

    @Test
    fun testParseGitHubPrUrl_invalidUrls() {
        val testCases = listOf(
            "https://github.com/SayanthRock/FREE-AI-ROCK-",
            "not-a-url",
            "https://github.com/SayanthRock/FREE-AI-ROCK-/issues/1",
            "https://gitlab.com/SayanthRock/Root-apk/pull/123"
        )

        for (input in testCases) {
            val result = parseGitHubPrUrl(input)
            assertNull("Expected null on input: $input", result)
        }
    }

    @Test
    fun testExtractFileNameFromUrl_validUrls() {
        val testCases = listOf(
            "https://github.com/SayanthRock/FREE-AI-ROCK-/blob/main/app/src/main/java/com/sayanthrock/freeairock/MainActivity.kt" to "MainActivity.kt",
            "https://raw.githubusercontent.com/SayanthRock/FREE-AI-ROCK-/main/app/build.gradle.kts" to "build.gradle.kts",
            "https://github.com/owner/repo/blob/branch/src/App.tsx?someQuery=1" to "App.tsx",
            "github.com/owner/repo/blob/branch/src/index.html#anchor" to "index.html"
        )

        for ((input, expected) in testCases) {
            val result = extractFileNameFromUrl(input)
            assertEquals("Failed on input: $input", expected, result)
        }
    }

    @Test
    fun testExtractFileNameFromUrl_invalidUrls() {
        val testCases = listOf(
            "https://github.com/SayanthRock/FREE-AI-ROCK-",
            "https://github.com/SayanthRock/FREE-AI-ROCK-/tree/main/app",
            "not-a-file-url"
        )

        for (input in testCases) {
            val result = extractFileNameFromUrl(input)
            assertNull("Expected null on input: $input", result)
        }
    }
}

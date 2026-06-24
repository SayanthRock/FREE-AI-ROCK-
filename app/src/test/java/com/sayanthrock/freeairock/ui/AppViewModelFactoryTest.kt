package com.sayanthrock.freeairock.ui

import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppViewModelFactoryTest {

    private val secureStorage: SecureStorageManager = mockk(relaxed = true)
    private val apiService: GitHubApiService = mockk(relaxed = true)
    private lateinit var factory: AppViewModelFactory

    @Before
    fun setup() {
        every { secureStorage.getGeminiKey() } returns null
        factory = AppViewModelFactory(secureStorage, apiService)
    }

    @Test
    fun `creates AppViewModel`() {
        val viewModel = factory.create(AppViewModel::class.java)

        assertTrue(viewModel is AppViewModel)
    }

    @Test
    fun `creates ReviewViewModel`() {
        val viewModel = factory.create(ReviewViewModel::class.java)

        assertTrue(viewModel is ReviewViewModel)
    }

    @Test
    fun `creates ImageViewModel`() {
        val viewModel = factory.create(ImageViewModel::class.java)

        assertTrue(viewModel is ImageViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws for unknown ViewModel`() {
        factory.create(UnknownViewModel::class.java)
    }

    private class UnknownViewModel : androidx.lifecycle.ViewModel()
}

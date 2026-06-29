package com.sayanthrock.freeairock.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sayanthrock.freeairock.MainDispatcherRule
import com.sayanthrock.freeairock.data.ai.CodeAnalysisState
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ReviewViewModel
    private val secureStorage = SecureStorageManager()
    private val apiService: GitHubApiService = mockk(relaxed = true)

    @Before
    fun setup() {
        secureStorage.clearSecrets()
        viewModel = ReviewViewModel(secureStorage, apiService)
    }

    @Test
    fun `missing Gemini key updates state to Error`() = runTest {
        viewModel.run("SayanthRock", "Root-apk", "12")

        assertTrue(viewModel.state.value is CodeAnalysisState.Error)
    }

    @Test
    fun `invalid pull request number updates state to Error`() = runTest {
        secureStorage.saveGeminiKey("fake-test-key")

        viewModel.run("SayanthRock", "Root-apk", "not-a-number")

        assertTrue(viewModel.state.value is CodeAnalysisState.Error)
    }
}

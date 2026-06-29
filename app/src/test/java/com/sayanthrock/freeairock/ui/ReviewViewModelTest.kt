package com.sayanthrock.freeairock.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sayanthrock.freeairock.MainDispatcherRule
import com.sayanthrock.freeairock.data.ai.CodeAnalysisState
import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.github.GitHubContentItem
import com.sayanthrock.freeairock.data.github.GitHubRepo
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
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
    private val apiService = FakeGitHubApiService()

    @Before
    fun setup() {
        secureStorage.clearSecrets()
        viewModel = ReviewViewModel(secureStorage, apiService)
    }

    @Test
    fun `missing Gemini key updates state to Error`() = runTest {
        viewModel.run("owner", "repo", "12")
        assertTrue(viewModel.state.value is CodeAnalysisState.Error)
    }

    @Test
    fun `invalid pull request number updates state to Error`() = runTest {
        secureStorage.saveGeminiKey("test-key")
        viewModel.run("owner", "repo", "not-a-number")
        assertTrue(viewModel.state.value is CodeAnalysisState.Error)
    }

    private class FakeGitHubApiService : GitHubApiService {
        override suspend fun getMyRepositories(sort: String, perPage: Int): List<GitHubRepo> = emptyList()
        override suspend fun getRepositoryContent(owner: String, repo: String, path: String): List<GitHubContentItem> = emptyList()
        override suspend fun getPullRequestDiff(owner: String, repo: String, pullNumber: Int): ResponseBody = "".toResponseBody()
        override suspend fun downloadRawFile(fileUrl: String): ResponseBody = "".toResponseBody()
    }
}

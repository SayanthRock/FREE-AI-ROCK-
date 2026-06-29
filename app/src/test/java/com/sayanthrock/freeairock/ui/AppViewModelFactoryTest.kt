package com.sayanthrock.freeairock.ui

import com.sayanthrock.freeairock.data.github.GitHubApiService
import com.sayanthrock.freeairock.data.github.GitHubContentItem
import com.sayanthrock.freeairock.data.github.GitHubRepo
import com.sayanthrock.freeairock.data.storage.SecureStorageManager
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppViewModelFactoryTest {

    private val secureStorage = SecureStorageManager()
    private val apiService = FakeGitHubApiService()
    private lateinit var factory: AppViewModelFactory

    @Before
    fun setup() {
        secureStorage.clearSecrets()
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

    private class FakeGitHubApiService : GitHubApiService {
        override suspend fun getMyRepositories(sort: String, perPage: Int): List<GitHubRepo> = emptyList()
        override suspend fun getRepositoryContent(owner: String, repo: String, path: String): List<GitHubContentItem> = emptyList()
        override suspend fun getPullRequestDiff(owner: String, repo: String, pullNumber: Int): ResponseBody = "".toResponseBody()
        override suspend fun downloadRawFile(fileUrl: String): ResponseBody = "".toResponseBody()
    }
}

package com.sayanthrock.freeairock.data.github

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface GitHubApiService {

    @GET("user/repos")
    suspend fun getMyRepositories(
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int = 100
    ): List<GitHubRepo>

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepositoryContent(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path(value = "path", encoded = true) path: String = ""
    ): List<GitHubContentItem>

    @GET
    suspend fun downloadRawFile(
        @Url fileUrl: String
    ): ResponseBody
}

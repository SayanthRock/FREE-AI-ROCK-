package com.sayanthrock.freeairock.data.github

import com.google.gson.annotations.SerializedName

/**
 * Represents a GitHub repository returned from GET /user/repos.
 */
data class GitHubRepo(
    val id: Long,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("private") val isPrivate: Boolean,
    @SerializedName("html_url") val htmlUrl: String,
    val description: String?,
    @SerializedName("default_branch") val defaultBranch: String
)

/**
 * Represents a file or directory returned from GET /repos/{owner}/{repo}/contents/{path}.
 */
data class GitHubContentItem(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val type: String,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("download_url") val downloadUrl: String?
) {
    val isFile: Boolean get() = type == "file"
    val isDirectory: Boolean get() = type == "dir"
}

package com.zxltrxn.githubclient.data.model

import com.zxltrxn.githubclient.data.Resource

data class RepoDetails(
    val repo: Repo,
    val readme: Resource<String>
)

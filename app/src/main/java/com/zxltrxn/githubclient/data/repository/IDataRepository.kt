package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.domain.model.Repo


interface IDataRepository {
    suspend fun getRepositories(): Resource<List<Repo>>
    suspend fun getRepository(repoName: String): Resource<Repo>
    suspend fun getRepositoryReadme(
        repoName: String,
        branch: String
    ): Resource<String>
}
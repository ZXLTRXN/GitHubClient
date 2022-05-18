package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.domain.model.Repo


interface IDataRepository {
    suspend fun getRepositories(): Resource<List<Repo>>
    suspend fun getRepository(ownerName: String, repoName: String): Resource<Repo>
    suspend fun getRepositoryReadme(
        ownerName: String,
        repoName: String,
        branch: String
    ): Resource<String>
}
package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo

interface IDataRepository {
    suspend fun getRepositories(): Resource<List<Repo>>
//    suspend fun getRepository(repoId: String): RepoDetails
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String
}
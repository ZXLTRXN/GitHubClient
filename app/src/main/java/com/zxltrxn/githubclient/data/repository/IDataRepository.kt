package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.model.RepoDetails

interface IDataRepository {
    suspend fun getRepositories(): Resource<List<Repo>>
    suspend fun getRepository(repoId: Int): Resource<RepoDetails>
}
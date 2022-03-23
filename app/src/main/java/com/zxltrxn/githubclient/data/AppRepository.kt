package com.zxltrxn.githubclient.data

import android.util.Log
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.zxltrxn.githubclient.utils.NetworkUtils.tryRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val userStorage: UserStorage,
    private val api: APIService
) {
    private var repos = listOf<Repo>()

    suspend fun getRepositories(): Resource<List<Repo>> = withContext(Dispatchers.IO){
        val res = tryRequest{ api.getRepos() }
        if (res is NetworkResource.Success){
            repos = res.data ?: listOf<Repo>()
        }
        return@withContext res.toResource()

//        val res = api.getRepos()
//        return@withContext Resource.Error("aaaaaaaaaaaaa")

    }

//    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }
//
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }
}

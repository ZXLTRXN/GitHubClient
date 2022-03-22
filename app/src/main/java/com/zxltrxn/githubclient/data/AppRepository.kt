package com.zxltrxn.githubclient.data

import android.util.Log
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val userStorage: UserStorage,
    private val api: APIService
) {
//    suspend fun getRepositories(): List<Repo> {
//        // TODO:
//    }
//
//    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }
//
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }
//
}

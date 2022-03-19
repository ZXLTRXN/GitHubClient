package com.zxltrxn.githubclient.data

import com.zxltrxn.githubclient.data.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor() {
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
    suspend fun signIn(token: String): Resource<UserInfo> = withContext(Dispatchers.IO){
        delay(1000);
    return@withContext try{
        Resource.Success(
            data = UserInfo(name ="valerka", token = "wfjekbfwkefwfew1241")
        )
    }catch (e:Exception){
        Resource.Error(message = "no internet")
    }
    }

}

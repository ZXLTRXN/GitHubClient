package com.zxltrxn.githubclient.data.repository

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.NetworkResource
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserInfo
import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.utils.Constants.WRONG_TOKEN_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.zxltrxn.githubclient.utils.NetworkUtils.tryRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userStorage: UserStorage,
    private val api: APIService
) : IAuthRepository, IDataRepository {

    private var repos:List<Repo> = listOf()

    ////////////IDataRepository
    override suspend fun getRepositories(): Resource<List<Repo>> = withContext(Dispatchers.IO){
        return@withContext Resource.Success(data = repos)
    }

    //    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }
//
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }

    private fun addLanguageColor(repos: List<Repo>):List<Repo> {
        return try{
//            val json = context.assets.open("github_colors.json")
//                .bufferedReader().use { it.readText() }

            repos.map {
                it.languageColor ="#38761D"
                it
            }
        }catch (e: IOException){
            Log.e(TAG, "AppRepository.addLanguageColor: $e")
            repos
        }
    }

    ////////////IAuthRepository
    override suspend fun signIn(token: String?): Resource<Unit> = withContext(Dispatchers.IO){
        if (token == null){
            if (userStorage.getUser().token == null)
                return@withContext Resource.Error(message = "No saved token")
        }else{
            userStorage.saveUser(UserInfo(token = token))
        }
        val res = tryRequest{ api.getRepos() }
        return@withContext if (res is NetworkResource.Success){
            repos = addLanguageColor(res.data.orEmpty())

            Resource.Success(Unit)
        }else{
            if(res.code == WRONG_TOKEN_CODE) userStorage.saveUser(UserInfo(token = null, name = null))
            repos = listOf()
            res.toUnitResource()
        }
    }

    override suspend fun signOut() = withContext(Dispatchers.IO){
        userStorage.saveUser(UserInfo(token = null, name = null))
    }

}

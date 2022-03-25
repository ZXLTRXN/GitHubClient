package com.zxltrxn.githubclient.data.repository

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.NetworkResource
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.model.User
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserInfo
import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants
import com.zxltrxn.githubclient.utils.Constants.TAG
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
        val res = tryRequest{ api.getRepos() }
        return@withContext if (res is NetworkResource.Success){
            repos = addLanguageColor(res.data.orEmpty())
            Resource.Success(data = repos)
        }else
            res.toResource()
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

    // method for AuthFragment
    override suspend fun signIn(token: String): Resource<User> = withContext(Dispatchers.IO){
        userStorage.saveUser(UserInfo(token = token))
        val result = tryRequest { api.getUser() }

        when (result){
            is NetworkResource.Success -> userStorage.saveUser(
                UserInfo(token = token, name = result.data!!.login)
            )
            is NetworkResource.Error -> {
                if(result.code == Constants.WRONG_TOKEN_CODE)
                    userStorage.saveUser(UserInfo(token = null, name = null))
            }
        }
        return@withContext result.toResource()
    }

    // if token already saved (method for Activity)
    override suspend fun signIn(): Resource<User> = withContext(Dispatchers.IO){
        if (userStorage.getUser().token == null){
            return@withContext Resource.Error(message = "No saved token")
        }
        val res = tryRequest{ api.getUser() }.toResource()
        return@withContext res
    }

    override suspend fun signOut() = withContext(Dispatchers.IO){
        userStorage.saveUser(UserInfo(token = null, name = null))
    }

}

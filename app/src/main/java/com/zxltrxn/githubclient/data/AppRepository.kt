package com.zxltrxn.githubclient.data

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.zxltrxn.githubclient.utils.NetworkUtils.tryRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userStorage: UserStorage,
    private val api: APIService
) {
    private var repos:List<Repo> = listOf()

    suspend fun getRepositories(): Resource<List<Repo>> = withContext(Dispatchers.IO){
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
}

package com.zxltrxn.githubclient.data.repository

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.model.RepoDetails
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserInfo
import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.utils.Constants.WRONG_TOKEN_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.zxltrxn.githubclient.utils.NetworkUtils.tryRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userStorage: UserStorage,
    private val api: APIService
) : IAuthRepository, IDataRepository {

    private var requestResult: Resource<List<Repo>> = Resource.Error("")

    ////////////IDataRepository
    override suspend fun getRepositories(): Resource<List<Repo>> = withContext(Dispatchers.IO){
        return@withContext requestResult
    }

    override suspend fun getRepository(repoId: String): Resource<RepoDetails> {
        return Resource.Success(RepoDetails(Repo(),"hey"))
    }

    override suspend fun getRepositoryReadme(ownerName: String, repositoryName: String,
                                             branchName: String) : Resource<String> {
        return Resource.Success("hey")
    }

    private fun readFromAssets(): String?{
        return try{
            context.assets.open("github_colors.json").bufferedReader().use { it.readText() }
        }catch (e: IOException){
            Log.e(TAG, "AppRepository.readFromAssets: $e")
            null
        }
    }

    private fun addLanguageColor(repos: List<Repo>):List<Repo> {
        val colorsString: String = readFromAssets() ?: return repos
        val colors = try{
            JSONObject(colorsString)
        }catch (e: JSONException){
            Log.e(TAG, "AppRepository.addLanguageColor: $e")
            return repos
        }
        return repos.map {
            if (it.language != null){
                try{
                    it.languageColor = colors.getString(it.language)
                }catch (e:JSONException){  }
            }
            it
        }
    }

    ////////////IAuthRepository
    override suspend fun signIn(token: String?): Resource<Unit> = withContext(Dispatchers.IO){
        if (token != null){
            userStorage.saveUser(UserInfo(token = token))
        }
        val res = tryRequest{ api.getRepos() }
        requestResult = if (res is Resource.Success){
            Resource.Success(addLanguageColor(res.data.orEmpty()))
        }else{
            if(res.code == WRONG_TOKEN_CODE) clearUser()
            res
        }
        return@withContext res.toUnitResource()
    }

    override suspend fun signOut() = withContext(Dispatchers.IO){
        clearUser()
    }

    private fun clearUser(){
        userStorage.saveUser(UserInfo())
    }

}

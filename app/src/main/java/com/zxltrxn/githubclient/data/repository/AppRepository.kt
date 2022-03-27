package com.zxltrxn.githubclient.data.repository

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.model.RepoDetails
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.KeyValueStorage
import com.zxltrxn.githubclient.utils.Constants.BASE_URL_README
import com.zxltrxn.githubclient.utils.Constants.COLORS_FILE_NAME
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.utils.Constants.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.utils.NetworkUtils.okHttpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.zxltrxn.githubclient.utils.NetworkUtils.tryRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userStorage: KeyValueStorage,
    private val api: APIService,
    private val client: OkHttpClient
) : IAuthRepository, IDataRepository {

    private var repositoriesRequestResult: Resource<List<Repo>> = Resource.Error("")

    ////////////IDataRepository
    override suspend fun getRepositories(): Resource<List<Repo>> = withContext(Dispatchers.IO){
        return@withContext repositoriesRequestResult
    }

    override suspend fun getRepository(repoId: Int): Resource<RepoDetails> = withContext(Dispatchers.IO) {
        val repo = repositoriesRequestResult.data!!.find{ it.id == repoId }
        repo?.let {
            val ownerName: String = repo.owner?.name ?: ""
            val repoName: String = repo.name ?: ""
            val branchName: String = repo.branch ?: ""

            val readme = getRepositoryReadme(ownerName = ownerName,
                repositoryName = repoName, branchName = branchName)

            return@withContext Resource.Success(RepoDetails(repo,readme))
        }
        return@withContext Resource.Error("No repository with current id")
    }

    override suspend fun getRepositoryReadme(ownerName: String, repositoryName: String,
                                             branchName: String) : Resource<String> {
        val fileName = "README.md"
        val url = BASE_URL_README +
                "$ownerName/$repositoryName/$branchName/$fileName"
        return okHttpRequest(client, url)
    }

    private fun readFromAssets(): String?{
        return try{
            context.assets.open(COLORS_FILE_NAME).bufferedReader().use { it.readText() }
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
            userStorage.authToken = token
        }
        val res = tryRequest{ api.getRepos() }
        repositoriesRequestResult = if (res is Resource.Success){
            Resource.Success(addLanguageColor(res.data.orEmpty()))
        }else{
            if(res.code == WRONG_TOKEN_CODE) userStorage.clearUserData()
            res
        }
        return@withContext res.toUnitResource()
    }

    override suspend fun signOut() = withContext(Dispatchers.IO){
        userStorage.clearUserData()
    }

}

//    private var userNameRequestResult: Resource<Unit> = Resource.Error("initial")
//    private suspend fun saveUserNameFromApi(){
//        val res = tryRequest{ api.getUser() }
//        if (res is Resource.Success){
//            userStorage.userName = res.data!!.name
//        }
//        userNameRequestResult = res.toUnitResource()
//        Log.d(TAG, "saveUserNameFromApi: ${res.data?.name}")
//    }
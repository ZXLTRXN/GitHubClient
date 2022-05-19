package com.zxltrxn.githubclient.domain

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.model.RepoData
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.network.APIService.Companion.BASE_URL_README
import com.zxltrxn.githubclient.data.network.APIService.Companion.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.data.network.NetworkUtils.okHttpRequest
import com.zxltrxn.githubclient.data.network.NetworkUtils.tryRequest
import com.zxltrxn.githubclient.data.storage.KeyValueStorage
import com.zxltrxn.githubclient.domain.model.Repo

import com.zxltrxn.githubclient.utils.toRepo

import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userStorage: KeyValueStorage,
    private val api: APIService,
    private val client: OkHttpClient
) {
    private val COLORS_FILE_NAME = "github_colors.json"

    ////////////Data
    suspend fun getRepositories(): Resource<List<Repo>> =
        withContext(Dispatchers.IO) {
            val res: Resource<List<RepoData>> = tryRequest { api.getRepos() }
            return@withContext when (res) {
                is Resource.Success -> {
                    val repos = res.data.map { repoData ->
                        repoData.toRepo()
                    }
                    Resource.Success(data = addLanguageColor(repos))
                }
                is Resource.Error -> {
                    res
                }
            }
        }

    suspend fun getRepository(ownerName: String, repoName: String): Resource<Repo> =
        withContext(Dispatchers.IO) {
            val res: Resource<RepoData> = tryRequest { api.getRepo(ownerName, repoName) }
            return@withContext when (res) {
                is Resource.Success -> Resource.Success(data = res.data.toRepo())
                is Resource.Error -> res
            }
        }

    suspend fun getRepositoryReadme(
        ownerName: String,
        repoName: String,
        branch: String
    ): Resource<String> = withContext(Dispatchers.IO) {
        val fileName = "README.md"
        val url = "$BASE_URL_README$ownerName/$repoName/$branch/$fileName"
        return@withContext okHttpRequest(client, url)
    }

    ////////////Auth
    fun isTokenSaved(): Boolean = userStorage.authToken != null

    suspend fun signInWithSavedToken(): Resource<Unit> = signInRequest()

    suspend fun signIn(token: String): Resource<Unit> {
        userStorage.authToken = token
        return signInRequest()
    }

    fun signOut() = userStorage.clearUserData()

    private fun readFromAssets(): String? {
        return try {
            context.assets.open(COLORS_FILE_NAME).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "readFromAssets: $e")
            null
        }
    }

    private fun addLanguageColor(repos: List<Repo>): List<Repo> {
        val colorsString: String = readFromAssets() ?: return repos
        val colors = try {
            JSONObject(colorsString)
        } catch (e: JSONException) {
            Log.e(javaClass.simpleName, "addLanguageColor: $e")
            return repos
        }
        return repos.map {
            if (it.language != null) {
                try {
                    it.languageColor = colors.getString(it.language)
                } catch (e: JSONException) {
                }
            }
            it
        }
    }

    private suspend fun signInRequest(): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val res: Resource<RepoData.Owner> = tryRequest { api.getUser() }
            return@withContext when (res) {
                is Resource.Success -> {
                    userStorage.userName = res.data.name
                    Resource.Success(Unit)
                }
                is Resource.Error -> {
                    if (res.code == WRONG_TOKEN_CODE) userStorage.clearUserData()
                    res
                }
            }
        }
}
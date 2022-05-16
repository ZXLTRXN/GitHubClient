package com.zxltrxn.githubclient.domain

import android.content.Context
import android.util.Log
import com.zxltrxn.githubclient.data.model.RepoData
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.network.APIService.Companion.BASE_URL_README
import com.zxltrxn.githubclient.data.network.APIService.Companion.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.data.network.NetworkUtils.okHttpRequest
import com.zxltrxn.githubclient.data.network.NetworkUtils.tryRequest
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.data.repository.IDataRepository
import com.zxltrxn.githubclient.data.storage.KeyValueStorage
import com.zxltrxn.githubclient.domain.model.Repo
import com.zxltrxn.githubclient.domain.model.UserInfo
import com.zxltrxn.githubclient.utils.toRepo
import com.zxltrxn.githubclient.utils.toUserInfo
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
) : IAuthRepository, IDataRepository {
    private val COLORS_FILE_NAME = "github_colors.json"

    ////////////IDataRepository
    override suspend fun getRepositories(): Resource<List<Repo>> =
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

    override suspend fun getRepository(repoId: Int): Resource<Repo> =
        withContext(Dispatchers.IO) {

//            val repo = repositoriesRequestResult.data!!.find { it.id == repoId }
//            repo?.let {
//                val ownerName: String = repo.owner.name
//                val repoName: String = repo.name
//                val branchName: String = repo.branch
//
//                val readme = getRepositoryReadme(
//                    ownerName = ownerName,
//                    repositoryName = repoName, branchName = branchName
//                )
//                return@withContext Resource.Success(RepoDetails(repo, readme))
//            }
            return@withContext Resource.Error(LocalizeString.Raw("No repository with current id"))
        }

    override suspend fun getRepositoryReadme(
        ownerName: String, repositoryName: String,
        branchName: String
    ): Resource<String> {
        val fileName = "README.md"
        val url = "$BASE_URL_README$ownerName/$repositoryName/$branchName/$fileName"
        return okHttpRequest(client, url)
    }

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

    ////////////IAuthRepository
    override fun isTokenSaved(): Boolean = userStorage.authToken != null

    override suspend fun signIn(token: String?): Resource<UserInfo> =
        withContext(Dispatchers.IO) {
            token?.let {
                userStorage.authToken = it
            }
            val res: Resource<RepoData.Owner> = tryRequest { api.getUser() }
            return@withContext when (res) {
                is Resource.Success -> {
                    userStorage.userName = res.data.name
                    Resource.Success(data = res.data.toUserInfo())
                }
                is Resource.Error -> {
                    if (res.code == WRONG_TOKEN_CODE) userStorage.clearUserData()
                    res
                }
            }
        }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        userStorage.clearUserData()
    }
}
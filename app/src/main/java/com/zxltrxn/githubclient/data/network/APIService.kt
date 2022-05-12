package com.zxltrxn.githubclient.data.network

import com.zxltrxn.githubclient.data.model.Repo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/user")
    suspend fun getUser(): Response<Repo.Owner>

    @GET("/users/icerockdev/repos") //for test
//    @GET("/user/repos")
    suspend fun getRepos(
        @Query("sort") sort: String = "updated",
        @Query("per_page") amount: Int = REPOSITORIES_AMOUNT
    ): Response<List<Repo>>

//    @GET("/repos/{owner}/{repo}")
//    suspend fun getRepo():Response<String>

    companion object{
        const val BASE_URL = "https://api.github.com/"
        const val BASE_URL_README = "https://raw.githubusercontent.com/"
        const val WRONG_TOKEN_CODE = 401
        const val REPOSITORIES_AMOUNT = 10
    }
}
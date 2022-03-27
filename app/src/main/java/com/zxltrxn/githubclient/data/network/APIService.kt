package com.zxltrxn.githubclient.data.network

import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.model.User
import com.zxltrxn.githubclient.utils.Constants.REPOSITORIES_AMOUNT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("/user")
    suspend fun getUser(): Response<User>


    @GET("/users/icerockdev/repos")
//    @GET("/user/repos")
    suspend fun getRepos(
        @Query("sort") sort:String = "updated",
        @Query("per_page") amount:Int = REPOSITORIES_AMOUNT
    ):Response<List<Repo>>

//    @GET("/repos/{owner}/{repo}")
//    suspend fun getRepo():Response<String>

    @GET("https://raw.githubusercontent.com/repos/{owner}/{repo}/{branch}/{path}")
    suspend fun getReadme(
        @Path("owner") ownerName: String,
        @Path("repo") repositoryName: String,
        @Path("branch") branchName: String,
        @Path("path") file: String = "README.md"
    ):Response<String>
}
package com.zxltrxn.githubclient.data.network

import com.zxltrxn.githubclient.data.model.User
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("/user")
    suspend fun getUser(): Response<User>
}
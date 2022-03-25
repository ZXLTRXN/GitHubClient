package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.User

interface IAuthRepository {
    suspend fun signIn(): Resource<User>
    suspend fun signIn(token: String): Resource<User>
    suspend fun signOut()
}
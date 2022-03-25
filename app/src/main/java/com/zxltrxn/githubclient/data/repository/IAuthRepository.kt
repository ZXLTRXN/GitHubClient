package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.data.Resource

interface IAuthRepository {
    suspend fun signIn(token: String? = null): Resource<Unit>
    suspend fun signOut()
}
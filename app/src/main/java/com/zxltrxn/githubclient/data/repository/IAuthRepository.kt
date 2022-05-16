package com.zxltrxn.githubclient.data.repository

import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.domain.model.UserInfo

interface IAuthRepository {
    fun isTokenSaved(): Boolean
    suspend fun signIn(token: String?): Resource<UserInfo>
    suspend fun signOut()
}
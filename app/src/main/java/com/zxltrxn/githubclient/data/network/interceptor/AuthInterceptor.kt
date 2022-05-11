package com.zxltrxn.githubclient.data.network.interceptor

import com.zxltrxn.githubclient.data.storage.KeyValueStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val userStorage: KeyValueStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = userStorage.authToken
        val request = token?.let { t ->
            chain.request().newBuilder()
                .header("Authorization", "token $t")
                .build()
        } ?: chain.request()
        return chain.proceed(request)
    }
}

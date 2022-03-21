package com.zxltrxn.githubclient.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AcceptInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            return chain.proceed(request)
    }
}
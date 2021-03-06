package com.zxltrxn.githubclient.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.network.APIService.Companion.BASE_URL
import com.zxltrxn.githubclient.data.network.interceptor.AcceptInterceptor
import com.zxltrxn.githubclient.data.network.interceptor.AuthInterceptor
import com.zxltrxn.githubclient.data.storage.KeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @AuthInterceptorOkHttpClient
    @Singleton
    @Provides
    fun providesAuthInterceptor(userStorage: KeyValueStorage): Interceptor =
        AuthInterceptor(userStorage)

    @AcceptInterceptorOkHttpClient
    @Singleton
    @Provides
    fun providesAcceptInterceptor(): Interceptor = AcceptInterceptor()

    @Singleton
    @Provides
    @OkHttpClientWithInterceptors
    fun provideOkHttpClientWithInterceptors(
        @AcceptInterceptorOkHttpClient acceptInterceptor: Interceptor,
        @AuthInterceptorOkHttpClient authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(acceptInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Singleton
    @Provides
    @kotlinx.serialization.ExperimentalSerializationApi
    fun provideAPIService(
        @OkHttpClientWithInterceptors client: OkHttpClient,
        json: Json
    ): APIService {
        val contentType = MediaType.get("application/json")
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()

        return retrofit.create(APIService::class.java)
    }
}
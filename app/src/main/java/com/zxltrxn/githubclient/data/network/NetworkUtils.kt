package com.zxltrxn.githubclient.data.network

import android.util.Log
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.data.network.APIService.Companion.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.domain.LocalizeString
import java.io.IOException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response

object NetworkUtils {
    suspend fun <T> tryRequest(request: suspend () -> Response<T>): Resource<T> {
        val errorMessage: Int = try {
            val response = request.invoke()
            if (response.isSuccessful) {
                return response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error(LocalizeString.Resource(R.string.empty_server_data))
            }
            return if (response.code() == WRONG_TOKEN_CODE)
                Resource.Error(LocalizeString.Resource(R.string.wrong_token), WRONG_TOKEN_CODE)
            else
                Resource.Error(LocalizeString.Resource(R.string.service_unavailable))
        } catch (e: SerializationException) {
            Log.e(javaClass.simpleName, "tryRequest: $e")
            R.string.service_unavailable
        } catch (e: UnknownHostException) {
            R.string.network_error
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "tryRequest: $e")
            R.string.unknown_error
        }
        return Resource.Error(LocalizeString.Resource(errorMessage))
    }

    fun okHttpRequest(client: OkHttpClient, url: String): Resource<String> {
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)

        val errorMessage: Int = try {
            val response = call.execute()

            return if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it.string())
                } ?: Resource.Error(LocalizeString.Resource(R.string.empty_server_data))
            } else {
                Resource.Error(LocalizeString.Resource(R.string.empty_server_data), code = response.code())
            }
        } catch (e: UnknownHostException) {
            R.string.network_error
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "okHttpRequest: $e")
            R.string.empty_server_data
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "tryRequest: $e")
            R.string.unknown_error
        }
        return  Resource.Error(LocalizeString.Resource(errorMessage))
    }
}
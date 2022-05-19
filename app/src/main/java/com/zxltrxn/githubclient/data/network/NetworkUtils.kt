package com.zxltrxn.githubclient.data.network

import android.util.Log
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.network.APIService.Companion.NO_RIGHTS_CODE
import com.zxltrxn.githubclient.data.network.APIService.Companion.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.domain.LocalizeString
import com.zxltrxn.githubclient.domain.Resource
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response

object NetworkUtils {
    val TAG = javaClass.simpleName
    const val NO_INTERNET_CODE = 0

    suspend fun <T> tryRequest(request: suspend () -> Response<T>): Resource<T> {
        var errorCode: Int? = null
        val errorMessage: Int = try {
            val response = request.invoke()
            if (response.isSuccessful) {
                return response.body()?.let { responseData ->
                    Resource.Success(responseData)
                } ?: Resource.Error(LocalizeString.Resource(R.string.empty_server_data))
            }
            when (response.code()) {
                WRONG_TOKEN_CODE -> {
                    errorCode = WRONG_TOKEN_CODE
                    R.string.wrong_token
                }
                NO_RIGHTS_CODE -> {
                    errorCode = NO_RIGHTS_CODE
                    R.string.no_rights
                }
                else -> R.string.service_unavailable
            }
        } catch (e: SerializationException) {
            Log.e(TAG, "tryRequest: $e")
            R.string.service_unavailable
        } catch (e: UnknownHostException) {
            errorCode = NO_INTERNET_CODE
            R.string.network_error
        } catch (e: SocketTimeoutException) {
            errorCode = NO_INTERNET_CODE
            R.string.network_error
        } catch (e: Exception) {
            Log.e(TAG, "tryRequest: $e")
            R.string.unknown_error
        }
        return Resource.Error(LocalizeString.Resource(errorMessage), code = errorCode)
    }

    fun okHttpRequest(client: OkHttpClient, url: String): Resource<String> {
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)

        var errorCode: Int? = null
        val errorMessage: Int = try {
            val response = call.execute()

            if (response.isSuccessful) {
                response.body()?.let { data ->
                    return Resource.Success(data.string())
                }
            } else {
                errorCode = response.code()
            }
            R.string.empty_server_data
        } catch (e: UnknownHostException) {
            errorCode = NO_INTERNET_CODE
            R.string.network_error
        } catch (e: SocketTimeoutException) {
            errorCode = NO_INTERNET_CODE
            R.string.network_error
        } catch (e: IOException) {
            Log.e(TAG, "okHttpRequest: $e")
            R.string.empty_server_data
        } catch (e: Exception) {
            Log.e(TAG, "tryRequest: $e")
            R.string.unknown_error
        }
        return Resource.Error(LocalizeString.Resource(errorMessage), errorCode)
    }
}
package com.zxltrxn.githubclient.utils

import android.util.Log
import com.zxltrxn.githubclient.data.NetworkResource
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.lang.Exception
import java.net.UnknownHostException

object NetworkUtils {
    suspend fun <T>tryRequest(request: suspend () -> Response<T>): NetworkResource<T> {
        return try{
            val response = request.invoke()
            if (response.isSuccessful){
                return response.body()?.let {
                    NetworkResource.Success(it)
                } ?: return NetworkResource.Error("Empty data from server")
            }
            Log.d(Constants.TAG, "tryRequest: ${response.code()}")
            when (response.code()){
                Constants.WRONG_TOKEN_CODE -> NetworkResource.Error("Wrong token", code = Constants.WRONG_TOKEN_CODE)
                else -> NetworkResource.Error("Service Unavailable")
            }
        }catch (e: SerializationException){
            NetworkResource.Error("Data from server unreadable")
        }catch (e: UnknownHostException){
            NetworkResource.Error("No internet connection")
        }catch (e: CancellationException){
            NetworkResource.Error("Cancelled")
        }catch (e: Exception){
            Log.e(Constants.TAG, "NetworkUtils.tryRequest: ${e}" )
            NetworkResource.Error("Unknown error")
        }
    }
}
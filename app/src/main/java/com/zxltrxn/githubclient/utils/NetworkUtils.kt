package com.zxltrxn.githubclient.utils

import android.util.Log
import com.zxltrxn.githubclient.data.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.lang.Exception
import java.net.UnknownHostException

object NetworkUtils {
    suspend fun <T>tryRequest(request: suspend () -> Response<T>): Resource<T> {
        return try{
            val response = request.invoke()
            if (response.isSuccessful){
                return response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty data from server")
            }
            when (response.code()){
                Constants.WRONG_TOKEN_CODE -> Resource.Error("Wrong token", code = Constants.WRONG_TOKEN_CODE)
                else -> Resource.Error("Service Unavailable")
            }
        }catch (e: SerializationException){
            Log.e(Constants.TAG, "NetworkUtils.tryRequest: ${e}" )
            Resource.Error("Data from server unreadable")
        }catch (e: UnknownHostException){
            Resource.Error("No internet connection")
        }catch (e: CancellationException){
            Resource.Error("Cancelled")
        }catch (e: Exception){
            Log.e(Constants.TAG, "NetworkUtils.tryRequest: ${e}" )
            Resource.Error("Unknown error")
        }
    }
}
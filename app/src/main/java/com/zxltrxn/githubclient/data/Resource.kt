package com.zxltrxn.githubclient.data

sealed class Resource<T>(val data:T? = null, val message:String? = null){
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(message:String):Resource<T>(message = message)
}

sealed class NetworkResource<T>(val data:T? = null, val message:String? = null, val code:Int? = null){
    class Success<T>(data:T):NetworkResource<T>(data)
    class Error<T>(message:String, code:Int? = null):NetworkResource<T>(message = message, code = code)

    fun toResource():Resource<T>{
        return when(this){
            is Success -> Resource.Success(data = data!!)
            is Error -> Resource.Error(message = message!!)
        }
    }

    fun toUnitResource():Resource<Unit>{
        return when(this){
            is Success -> Resource.Success(Unit)
            is Error -> Resource.Error(message = message!!)
        }
    }

}

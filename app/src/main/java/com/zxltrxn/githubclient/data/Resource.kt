package com.zxltrxn.githubclient.data

sealed class Resource<T>(val data: T? = null, val message: String? = null, val code: Int? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, code: Int? = null) : Resource<T>(message = message, code = code)

    fun toUnitResource(): Resource<Unit> {
        return when (this) {
            is Success -> Success(Unit)
            is Error -> Error(message = message!!, code = code)
        }
    }
}

//sealed interface Resource<T> {
//    data class Success<T>(val data: T) : Resource<T>
//    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>
//}

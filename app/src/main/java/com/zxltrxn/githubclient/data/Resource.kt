package com.zxltrxn.githubclient.data

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: LocalizeString, val code: Int? = null) : Resource<Nothing>
}

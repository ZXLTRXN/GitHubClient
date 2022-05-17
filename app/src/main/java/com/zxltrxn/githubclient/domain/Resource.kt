package com.zxltrxn.githubclient.domain

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: LocalizeString, val code: Int? = null) : Resource<Nothing>
}

package com.zxltrxn.githubclient.data.storage

interface UserStorage {
    fun getUser(): UserInfo
    fun saveUser(user: UserInfo)
}
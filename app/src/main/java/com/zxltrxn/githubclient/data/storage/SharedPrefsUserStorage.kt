package com.zxltrxn.githubclient.data.storage

import android.content.Context
import javax.inject.Inject

private const val SHARED_PREFS_NAME = "shared_prefs_user_data"

private const val KEY_NAME = "name"
private const val KEY_TOKEN = "token"

class SharedPrefsUserStorage @Inject constructor(context: Context): UserStorage {

    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun getUser(): UserInfo {
        val name = prefs.getString(KEY_NAME, null)
        val token = prefs.getString(KEY_TOKEN, null)
        return UserInfo(name = name, token = token)
    }

    override fun saveUser(user: UserInfo) {
        prefs.edit()
            .putString(KEY_NAME, user.name)
            .putString(KEY_TOKEN, user.token)
            .apply()
    }

}
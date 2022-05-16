package com.zxltrxn.githubclient.data.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class KeyValueStorage @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(token) {
            prefs.edit()
                .putString(KEY_TOKEN, token)
                .apply()
        }

    var userName: String?
        get() = prefs.getString(KEY_NAME, null)
        set(name) {
            prefs.edit()
                .putString(KEY_NAME, name)
                .apply()
        }

    fun clearUserData() {
        authToken = null
        userName = null
    }

    companion object{
        private const val SHARED_PREFS_NAME = "shared_prefs_user_data"
        private const val KEY_NAME = "name"
        private const val KEY_TOKEN = "token"
    }
}
package com.zxltrxn.githubclient.data

import android.util.Log
import com.zxltrxn.githubclient.data.model.User
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.storage.UserInfo

import com.zxltrxn.githubclient.data.storage.UserStorage
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.utils.Constants.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.utils.NetworkUtils.tryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class AuthMediator @Inject constructor(
//    private val userStorage: UserStorage,
//    private val api: APIService
//) {
//    // method for AuthFragment
//    suspend fun signIn(token: String): Resource<User> = withContext(Dispatchers.IO){
//        userStorage.saveUser(UserInfo(token = token))
//        val result = tryRequest { api.getUser() }
//
//        when (result){
//            is NetworkResource.Success -> userStorage.saveUser(
//                UserInfo(token = token, name = result.data!!.login))
//            is NetworkResource.Error -> {
//                if(result.code == WRONG_TOKEN_CODE)
//                    userStorage.saveUser(UserInfo(token = null, name = null))
//            }
//        }
////        Log.d(TAG, "signIn: ${userStorage.getUser()}")
//        return@withContext result.toResource()
//    }
//
//    // if token already saved (method for Activity)
//    suspend fun signIn(): Resource<User> = withContext(Dispatchers.IO){
//        if (userStorage.getUser().token == null){
//            return@withContext Resource.Error(message = "No saved token")
//        }
//        val res = tryRequest{ api.getUser() }.toResource()
////        Log.d(TAG, "AuthMediator.signIn: $res")
//        return@withContext res
//    }
//
//    suspend fun signOut() = withContext(Dispatchers.IO){
//       userStorage.saveUser(UserInfo(token = null, name = null))
//    }
//}
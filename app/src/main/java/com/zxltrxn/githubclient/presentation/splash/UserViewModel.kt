package com.zxltrxn.githubclient.presentation.splash

import androidx.lifecycle.ViewModel
import com.zxltrxn.githubclient.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: AppRepository) : ViewModel() {
//        val user: = LiveData<User>()

}
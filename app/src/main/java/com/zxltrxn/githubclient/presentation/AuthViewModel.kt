package com.zxltrxn.githubclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: IAuthRepository
) : ViewModel() {

    fun isAuthenticated(): Boolean {
        return repository.isTokenSaved()
    }

    fun signIn(token: String) {
        viewModelScope.launch {
            repository.signIn(token)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}
package com.zxltrxn.githubclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.domain.LocalizeString
import com.zxltrxn.githubclient.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: IAuthRepository
) : ViewModel() {
    private var _state: State = State.NotReady
    val state get() = _state

    fun trySignInWithSaved() {
        if (repository.isTokenSaved()) {
            viewModelScope.launch {
                val res = repository.signInWithSavedToken()
                _state = when (res) {
                    is Resource.Success -> State.Authenticated
                    is Resource.Error -> State.NotAuthenticated(res.message)
                }
            }
        } else {
            _state = State.NotAuthenticated(LocalizeString.Resource(R.string.no_saved_token))
        }
    }

    fun signIn(token: String) {
        viewModelScope.launch {
            _state = when (val res = repository.signIn(token)) {
                is Resource.Success -> State.Authenticated
                is Resource.Error -> State.NotAuthenticated(res.message)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _state = State.NotAuthenticated(LocalizeString.Resource(R.string.no_saved_token))
        }
    }

    sealed interface State {
        object NotReady : State
        object Authenticated : State
        data class NotAuthenticated(val message: LocalizeString) : State
    }
}
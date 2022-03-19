package com.zxltrxn.githubclient.presentation.auth

import android.util.Log
import androidx.lifecycle.*
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.data.AppRepository
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.UserInfo
import com.zxltrxn.githubclient.utils.validateToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository : AppRepository
    ) : ViewModel() {

    val token = MutableStateFlow("")

    private val _state:MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state = _state.asStateFlow()

    private val _actions = MutableSharedFlow<Action>()
    val actions = _actions.asSharedFlow()


    fun onSignButtonPressed() {
        Log.d(TAG, "onSignButtonPressed: ${token.value}")

        when (token.value.validateToken()){
            ValidationState.INVALID ->{
                _state.value = (State.InvalidInput("Invalid"))
            }
            ValidationState.EMPTY ->{
                _state.value = (State.InvalidInput("Empty"))
            }
            ValidationState.VALID ->{
                trySignIn()
            }
        }
    }

    private fun trySignIn(){
        viewModelScope.launch {
            _state.value = State.Loading
            when (val res = repository.signIn(token.value)){
                is Resource.Success -> {
                    _state.value = State.Idle
                    _actions.emit(Action.RouteToMain)
                }
                is Resource.Error -> {
                    val msg = res.message ?: "unknown error"
                    _state.value = State.Idle
                    _actions.emit(Action.ShowError(msg))
                }
            }
        }
    }

    sealed interface State{
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }


    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }

    enum class ValidationState{
        EMPTY,
        INVALID,
        VALID
    }
}
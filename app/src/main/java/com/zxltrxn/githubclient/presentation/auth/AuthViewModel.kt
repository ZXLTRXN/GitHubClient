package com.zxltrxn.githubclient.presentation.auth

import androidx.lifecycle.*
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.utils.validateToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo : IAuthRepository
    ) : ViewModel() {

    val token = MutableStateFlow("")

    private val _state:MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state = _state.asStateFlow()

    private val _actions = MutableSharedFlow<Action>()
    val actions = _actions.asSharedFlow()


    fun onSignButtonPressed() {
        when (val validationResult = token.value.validateToken()){
            ValidationState.INVALID ->{
                _state.value = (State.InvalidInput(validationResult.reason))
            }
            ValidationState.EMPTY ->{
                _state.value = (State.InvalidInput(validationResult.reason))
            }
            ValidationState.VALID ->{
                trySignIn()
            }
        }
    }

    private fun trySignIn(){
        viewModelScope.launch {
            _state.value = State.Loading
            when (val res = authRepo.signIn(token.value)){
                is Resource.Success -> {
                    _actions.emit(Action.RouteToMain)
                    _state.value = State.Idle
                }
                is Resource.Error -> {
                    _state.value = State.Idle
                    _actions.emit(Action.ShowError(res.message!!))
                }
            }
        }
    }

    sealed interface State{
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State

        fun getErrorReason(): String? = if (this is InvalidInput) this.reason else null
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }

    enum class ValidationState(val reason: String){
        EMPTY("Empty input"),
        INVALID("Invalid input"),
        VALID("Valid input")
    }
}
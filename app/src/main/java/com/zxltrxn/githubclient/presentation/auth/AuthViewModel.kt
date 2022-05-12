package com.zxltrxn.githubclient.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.utils.validateToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: IAuthRepository
) : ViewModel() {

    val token: MutableStateFlow<String> = MutableStateFlow("")

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state = _state.asStateFlow()

    private val _actions = MutableSharedFlow<Action>()
    val actions = _actions.asSharedFlow()

    init {
        viewModelScope.launch {
            token.collectLatest {
                validate()
            }
        }
    }

    fun onSignButtonPressed() {
        when (val validationResult = validate()) {
            ValidationState.VALID -> {
                trySignIn()
            }
            /* чтобы сообщение о пустом поле не горело со старта
            а лишь после нажатия кнопки */
            ValidationState.EMPTY -> {
                _state.value = (State.InvalidInput(validationResult.reason))
            }
            else -> {
            }
        }
    }

    private fun validate(): ValidationState {
        val validationResult = token.value.validateToken()
        when (validationResult) {
            ValidationState.INVALID -> {
                _state.value = (State.InvalidInput(validationResult.reason))
            }
            ValidationState.VALID -> {
                _state.value = State.Idle
            }
            ValidationState.EMPTY -> {
            }
        }
        return validationResult
    }

//    when (val validationResult = token.value.validateToken()){
//        ValidationState.INVALID ->{
//            _state.value = (State.InvalidInput(validationResult.reason))
//        }
//        ValidationState.EMPTY ->{
//            _state.value = (State.InvalidInput(validationResult.reason))
//        }
//        ValidationState.VALID ->{
//            trySignIn()
//        }
//    }

    private fun trySignIn() {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val res = authRepo.signIn(token.value)) {
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

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State

        fun getErrorReason(): String? = if (this is InvalidInput) this.reason else null
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }

    enum class ValidationState(val reason: String) {
        EMPTY("Empty input"),
        INVALID("Invalid input"),
        VALID("Valid input")
    }
}
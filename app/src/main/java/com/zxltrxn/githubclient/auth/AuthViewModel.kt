package com.zxltrxn.githubclient.auth

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zxltrxn.githubclient.Constants.TAG
import com.zxltrxn.githubclient.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(repository : AppRepository) : ViewModel() {
  val token: MutableLiveData<String> = MutableLiveData("")
    /*
      val state: LiveData<State>
      val actions: Flow<Action>*/

    fun onSignButtonPressed() {
        Log.d(TAG, "onSignButtonPressed: ${token.value}")
    }

    private fun validateToken(){

    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }
}
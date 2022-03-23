package com.zxltrxn.githubclient.presentation.repositoriesList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.data.AppRepository
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    init{
        getRepos()
    }

    private fun getRepos(){
        viewModelScope.launch {
            when (val res = repository.getRepositories()){
                is Resource.Success -> {
                    if (res.data!!.isEmpty()){
                        _state.value = State.Empty
                    }else{
                        _state.value = State.Loaded(repos = res.data)
                    }
                }
                is Resource.Error ->{
                    _state.value = State.Error(res.message!!)
                }
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }
}

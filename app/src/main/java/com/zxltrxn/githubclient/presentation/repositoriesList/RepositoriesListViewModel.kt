package com.zxltrxn.githubclient.presentation.repositoriesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.network.NetworkUtils
import com.zxltrxn.githubclient.domain.AppRepository
import com.zxltrxn.githubclient.domain.LocalizeString
import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    init {
        getRepos()
    }

    fun signOut() = repository.signOut()

    fun retry() = getRepos()

    private fun getRepos() {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val res = repository.getRepositories()) {
                is Resource.Success -> {
                    if (res.data.isEmpty()) {
                        _state.value = State.Empty
                    } else {
                        _state.value = State.Loaded(repos = res.data)
                    }
                }
                is Resource.Error -> {
                    var icon = R.drawable.ic_something_error
                    val label = when (res.code) {
                        NetworkUtils.NO_INTERNET_CODE -> {
                            icon = R.drawable.ic_network_error
                            LocalizeString.Resource(R.string.network_error_label)
                        }
                        else -> LocalizeString.Resource(R.string.something_error_label)
                    }
                    _state.value = State.Error(icon, label, res.message)
                }
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(
            val errorIcon: Int,
            val errorLabel: LocalizeString,
            val errorMessage: LocalizeString
        ) : State

        object Empty : State
    }
}

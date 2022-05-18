package com.zxltrxn.githubclient.presentation.detailInfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.repository.IDataRepository
import com.zxltrxn.githubclient.domain.LocalizeString
import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: IDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    init {
        val ownerName: String? = savedStateHandle.get<String>("ownerName")
        val repoName: String? = savedStateHandle.get<String>("repoName")
        val branch: String? = savedStateHandle.get<String>("branch")
        if (ownerName != null && repoName != null && branch != null) {
            getInfo(ownerName, repoName, branch)
        } else {
            _state.value = State.Error(LocalizeString.Resource(R.string.unknown_error))
            Log.e(javaClass.simpleName, "init: no arguments in ViewModel")
        }
    }

    fun signOut() {

    }

    private fun getInfo(ownerName: String, repoName: String, branch: String) {
        viewModelScope.launch {
            val repoRes: Resource<Repo> = repository.getRepository(ownerName, repoName)
            val readmeRes: Resource<String> = repository.getRepositoryReadme(ownerName, repoName, branch)
            when (repoRes) {
                is Resource.Success -> {
                    val readmeState = when (readmeRes) {
                        is Resource.Success -> {
                            if (readmeRes.data.isEmpty()) ReadmeState.Empty
                            else ReadmeState.Loaded(readmeRes.data)
                        }
                        is Resource.Error -> {
                            if (readmeRes.code == APIService.NOT_FOUND) ReadmeState.Empty
                            else ReadmeState.Error(readmeRes.message)
                        }
                    }
                    _state.value = State.Loaded(repoRes.data, readmeState)
                }
                is Resource.Error -> {
                    _state.value = State.Error(repoRes.message)
                }
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: LocalizeString) : State

        data class Loaded(
            val githubRepo: Repo,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: LocalizeString) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }
}
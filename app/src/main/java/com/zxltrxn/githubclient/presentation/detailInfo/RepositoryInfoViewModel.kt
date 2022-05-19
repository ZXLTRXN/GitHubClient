package com.zxltrxn.githubclient.presentation.detailInfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.network.NetworkUtils.NO_INTERNET_CODE
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
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    private val ownerName: String? = savedStateHandle.get<String>("ownerName")
    private val repoName: String? = savedStateHandle.get<String>("repoName")
    private val branch: String? = savedStateHandle.get<String>("branch")

    init {
        tryGetInfo()
    }

    fun signOut() = repository.signOut()

    fun retry() = tryGetInfo()

    private fun tryGetInfo() {
        if (ownerName != null && repoName != null && branch != null) {
            getInfo(ownerName, repoName, branch)
        } else {
            _state.value = State.Error(
                R.drawable.ic_something_error,
                LocalizeString.Resource(R.string.something_error_label),
                LocalizeString.Resource(R.string.unknown_error)
            )
            Log.e(javaClass.simpleName, "tryGetInfo: no arguments in ViewModel")
        }
    }

    private fun getInfo(ownerName: String, repoName: String, branch: String) {
        viewModelScope.launch {
            val repoRes: Resource<Repo> = repository.getRepository(ownerName, repoName)
            val readmeRes: Resource<String> =
                repository.getRepositoryReadme(ownerName, repoName, branch)
            when (repoRes) {
                is Resource.Success -> {
                    val readmeState = getReadmeState(readmeRes)
                    _state.value = State.Loaded(repoRes.data, readmeState)
                }
                is Resource.Error -> {
                    var icon = R.drawable.ic_something_error
                    val label = when (repoRes.code) {
                        NO_INTERNET_CODE -> {
                            icon = R.drawable.ic_network_error
                            LocalizeString.Resource(R.string.network_error_label)
                        }
                        else -> LocalizeString.Resource(R.string.something_error_label)
                    }
                    _state.value = State.Error(icon, label, repoRes.message)
                }
            }
        }
    }

    private fun getReadmeState(readmeRes: Resource<String>): ReadmeState {
        when (readmeRes) {
            is Resource.Success -> {
                return if (readmeRes.data.isEmpty()) ReadmeState.Empty
                else ReadmeState.Loaded(readmeRes.data)
            }
            is Resource.Error -> {
                var icon = R.drawable.ic_something_error
                val label: LocalizeString = when (readmeRes.code) {
                    APIService.NOT_FOUND_CODE -> return ReadmeState.Empty
                    NO_INTERNET_CODE -> {
                        icon = R.drawable.ic_network_error
                        LocalizeString.Resource(R.string.network_error_label)
                    }
                    else -> LocalizeString.Resource(R.string.something_error_label)
                }
                return ReadmeState.Error(icon, label, readmeRes.message)
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(
            val errorIcon: Int,
            val errorLabel: LocalizeString,
            val errorMessage: LocalizeString
        ) : State

        data class Loaded(
            val githubRepo: Repo,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(
            val errorIcon: Int,
            val errorLabel: LocalizeString,
            val errorMessage: LocalizeString
        ) : ReadmeState

        data class Loaded(val markdown: String) : ReadmeState
    }


}
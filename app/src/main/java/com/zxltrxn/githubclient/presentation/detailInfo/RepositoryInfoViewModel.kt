package com.zxltrxn.githubclient.presentation.detailInfo

import androidx.lifecycle.ViewModel
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.repository.IDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: IDataRepository
): ViewModel(){
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()






    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State

        data class Loaded(
            val githubRepo: Repo,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }
}
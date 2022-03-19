package com.zxltrxn.githubclient.presentation.repositoriesList

import androidx.lifecycle.ViewModel
import com.zxltrxn.githubclient.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository) : ViewModel() {

    val state: StateFlow<State> = MutableStateFlow(State.Loading)

    sealed interface State {
        object Loading : State
//        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }
}
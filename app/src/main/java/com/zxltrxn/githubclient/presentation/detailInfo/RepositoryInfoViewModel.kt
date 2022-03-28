package com.zxltrxn.githubclient.presentation.detailInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.data.repository.IDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: IDataRepository
): ViewModel(){
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    fun getInfo(repoId:Int){
        viewModelScope.launch {
            when (val repositoryResource = repository.getRepository(repoId)){
                is Resource.Success -> {
                    val readmeState = when (val readmeResource = repositoryResource.data!!.readme){
                        is Resource.Success ->{
                            if (readmeResource.data!!.isEmpty()) ReadmeState.Empty
                            else ReadmeState.Loaded(readmeResource.data)
                        }
                        is Resource.Error -> {
                            ReadmeState.Error(repositoryResource.data.readme.message!!)
                        }
                    }
                    _state.value = State.Loaded(repositoryResource.data.repo, readmeState)
                }
                is Resource.Error -> {
                    _state.value = State.Error(repositoryResource.message!!)
                }
            }
        }
    }

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
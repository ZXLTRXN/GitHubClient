package com.zxltrxn.githubclient.presentation.detailInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxltrxn.githubclient.domain.Resource
import com.zxltrxn.githubclient.data.repository.IDataRepository
import com.zxltrxn.githubclient.domain.LocalizeString
import com.zxltrxn.githubclient.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: IDataRepository
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    fun getInfo(repoId: Int) {
        viewModelScope.launch {
            when (val res: Resource<Repo> = repository.getRepository(repoId)) {
                is Resource.Success -> {

//                    val readmeState = when (val readmeResource = repositoryResource.data!!.readme) {
//                        is Resource.Success -> {
//                            if (readmeResource.data!!.isEmpty()) ReadmeState.Empty
//                            else ReadmeState.Loaded(readmeResource.data)
//                        }
//                        is Resource.Error -> {
//                            ReadmeState.Error(repositoryResource.data.readme.message!!)
//                        }
//                    }
                    val readmeState =  ReadmeState.Error(LocalizeString.Raw("net readme"))
                    _state.value = State.Loaded(res.data, readmeState)
                }
                is Resource.Error -> {
                    _state.value = State.Error(res.message)
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
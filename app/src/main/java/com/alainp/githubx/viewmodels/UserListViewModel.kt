package com.alainp.githubx.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alainp.githubx.data.GithubRepository
import com.alainp.githubx.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject internal constructor(
    private val githubRepository: GithubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        if (!savedStateHandle.contains(KEY_SAVED_STATE)) {
            savedStateHandle.set(KEY_SAVED_STATE, DEFAULT_USERS)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val users: Flow<PagingData<User>> = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<User>() },
        savedStateHandle.getLiveData<String>(KEY_SAVED_STATE)
            .asFlow()
            .flatMapLatest { githubRepository.getUsers() }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)


//    fun getUsers(): Flow<PagingData<User>> {
//        return githubRepository.getUsers(20).cachedIn(viewModelScope)
//    }

    companion object {
        const val KEY_SAVED_STATE = "users"
        const val DEFAULT_USERS = "users"
    }
}
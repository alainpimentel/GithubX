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

    private var _viewType = ViewType.LIST
        set(value) {
            field = value
            savedStateHandle.set(KEY_SAVED_VIEW_TYPE, viewType.type)
        }

    val viewType
        get() = _viewType

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

    init {
        if (!savedStateHandle.contains(KEY_SAVED_STATE)) {
            savedStateHandle.set(KEY_SAVED_STATE, DEFAULT_USERS)
        }
        if (!savedStateHandle.contains(KEY_SAVED_VIEW_TYPE)) {
            savedStateHandle.set(KEY_SAVED_VIEW_TYPE, viewType.type)
        }
    }

    fun toggleViewType() {
        _viewType =
            if (_viewType == ViewType.LIST) ViewType.GRID
            else ViewType.LIST

    }

    enum class ViewType(val type: Int) {
        LIST(ITEM_VIEW_TYPE_GRID),
        GRID(ITEM_VIEW_TYPE_LIST);

        companion object {
            fun from(type: Int): ViewType {
                return if (type == ITEM_VIEW_TYPE_GRID) GRID
                else LIST
            }
        }
    }

    companion object {
        private const val KEY_SAVED_STATE = "users"
        private const val DEFAULT_USERS = "users"
        private const val KEY_SAVED_VIEW_TYPE = "viewtype"
        private const val ITEM_VIEW_TYPE_LIST = 0
        private const val ITEM_VIEW_TYPE_GRID = 1

    }
}
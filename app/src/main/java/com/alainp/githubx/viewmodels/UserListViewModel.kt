package com.alainp.githubx.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alainp.githubx.data.GithubRepository
import com.alainp.githubx.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject internal constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    fun getUsers(): Flow<PagingData<User>> {
        return githubRepository.getUsers(20).cachedIn(viewModelScope)
    }
}
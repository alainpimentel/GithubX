package com.alainp.githubx.viewmodels

import androidx.lifecycle.*
import com.alainp.githubx.data.GithubRepository
import com.alainp.githubx.data.UserDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserDetailViewModel @AssistedInject internal constructor(
    private val githubRepository: GithubRepository,
    @Assisted private val username: String,
) : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail


    init {
        viewModelScope.launch(Dispatchers.IO) {
            githubRepository.getUserDetail(username).collect {
                _userDetail.postValue(it)
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: UserDetailViewModelFactory,
            username: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(username) as T
            }

        }
    }
}

@AssistedFactory
interface UserDetailViewModelFactory {
    fun create(username: String): UserDetailViewModel
}
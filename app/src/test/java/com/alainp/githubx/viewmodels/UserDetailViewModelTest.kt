package com.alainp.githubx.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alainp.githubx.data.GithubRepository
import com.alainp.githubx.utilities.getOrAwaitValue
import com.alainp.githubx.utilities.getTestUserDetail
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class UserDetailViewModelTest {

    private lateinit var userDetailViewModel: UserDetailViewModel

    private val testUser = getTestUserDetail(1L, "xorline", 2000)
    private val githubRepository: GithubRepository = Mockito.mock(GithubRepository::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getGetUsers() = runBlocking {
        Mockito.`when`(githubRepository.getUserDetail(Mockito.anyString())).thenReturn(
            flow { emit(testUser) })

        userDetailViewModel = UserDetailViewModel(githubRepository, testUser.login)

        val result = userDetailViewModel.userDetail.getOrAwaitValue()
        Assert.assertEquals(testUser, result)
    }

}
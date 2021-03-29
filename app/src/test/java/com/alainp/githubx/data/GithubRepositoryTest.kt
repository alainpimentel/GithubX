package com.alainp.githubx.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alainp.githubx.api.GithubService
import com.alainp.githubx.utilities.CoroutineTestRule
import com.alainp.githubx.utilities.getTestUserDetail
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class GithubRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val db: AppDatabase =
        Mockito.mock(AppDatabase::class.java)
    private val service: GithubService =
        Mockito.mock(GithubService::class.java)
    private val userDetailDao: UserDetailDao =
        Mockito.mock(UserDetailDao::class.java)

    private val testUser = getTestUserDetail(1L, "xorline", System.currentTimeMillis())

    @Test
    fun getUserDetail_returnsUser() = runBlocking {
        Mockito.`when`(db.userDetailDao()).thenReturn(userDetailDao)
        Mockito.`when`(db.userDetailDao().hasUserDetail(Mockito.anyString(), Mockito.anyLong()))
            .thenReturn(true)
        Mockito.`when`(db.userDetailDao().getUserDetail(Mockito.anyString()))
            .thenReturn( flow { emit(testUser) })
        val repository = GithubRepository(db, service)
        repository.getUserDetail(testUser.login).collect {
            Assert.assertEquals(testUser, it)
        }

    }

}
package com.alainp.githubx.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.alainp.githubx.api.GithubService
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(
    private val db: AppDatabase,
    private val service: GithubService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getUsers(pageSize: Int = 50) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PagedKeyRemoteMediator(db, service)
    ) {
        //todo get since
        db.userDao().getUsers()
    }.flow

    suspend fun getUserDetail(username: String): Flow<UserDetail> {
        refreshUserDetail(username)
        return db.userDetailDao().getUserDetail(username)
    }

    private suspend fun refreshUserDetail(username: String) {
        try {

            val currentTimeMillis = System.currentTimeMillis()
            val exists =
                db.userDetailDao().hasUserDetail(username, currentTimeMillis - TIMEOUT_DATA_MS)
            if (!exists) {
                Log.d(TAG, "User $username does not exist, fetching...")
                val response = service.getUserDetail(username)
                Log.d(TAG, "response User $response ...")
                val updatedUserDetail = response.copy(
                    lastUpdated = System.currentTimeMillis()
                )
                db.userDetailDao().insertAll(listOf(updatedUserDetail))
                Log.d(TAG, "User $username was fetched...!\n $updatedUserDetail")
            } else {
                Log.d(TAG, "User $username is CACHED!")
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Error gettign user $username. $exception")

        }
    }

    companion object {
        private const val TAG = "GithubRepoMessi"
        private const val TIMEOUT_DATA_MS = 5 * 60 * 1000
    }
}
package com.alainp.githubx.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.alainp.githubx.api.GithubService
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
}
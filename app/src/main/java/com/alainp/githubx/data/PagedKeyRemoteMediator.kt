package com.alainp.githubx.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alainp.githubx.api.GithubService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PagedKeyRemoteMediator(
    private val db: AppDatabase,
    private val service: GithubService,
    private val pageSize: Int
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        try {
            Log.d(TAG, "load $loadType")
            val loadKey: Long? = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        db.userRemoteKeyDao().remoteKey()
                    }

                    Log.d(TAG, "load APPEND $remoteKey")

                    remoteKey?.since
                }
            }
            val data: List<User> = service.getUsers(since = loadKey ?: 0, perpage = pageSize)
            val lastItem = data.lastOrNull()

            Log.d(TAG, "load got users $data")

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.userDao().deleteAll()
                    db.userRemoteKeyDao().deleteAll()
                }

                lastItem?.let { db.userRemoteKeyDao().insert(UserRemoteKey(since = it.id)) }
                db.userDao().insertAll(data)
            }

            return MediatorResult.Success(endOfPaginationReached = data.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "MediatorMessi"
    }
}
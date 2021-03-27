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
    private val service: GithubService
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        try {
            Log.d("messi", "load $loadType")
            val loadKey: Long? = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        db.userRemoteKeyDao().remoteKey()
                    }

                    Log.d("messi", "load APPEND $remoteKey")

                    remoteKey?.since
                }
            }
            val data: List<User> = service.getUsers(loadKey ?: 0)
            val lastItem = data.lastOrNull()

            Log.d("messi", "load got users $data")

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
}
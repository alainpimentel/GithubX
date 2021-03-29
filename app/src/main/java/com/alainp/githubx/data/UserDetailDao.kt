package com.alainp.githubx.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailDao {
    @Query("SELECT * FROM userDetail WHERE login = :username")
    fun getUserDetail(username: String): Flow<UserDetail>

    @Query("SELECT EXISTS(SELECT * FROM userDetail WHERE login = :username AND lastUpdated >= :timeout)")
    fun hasUserDetail(username: String, timeout: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserDetail>)
}
package com.alainp.githubx.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: UserRemoteKey)

    @Query("SELECT * FROM remote_keys where id = 1")
    suspend fun remoteKey(): UserRemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}
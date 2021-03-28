package com.alainp.githubx.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<User>)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getUsers(): PagingSource<Int, User>

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
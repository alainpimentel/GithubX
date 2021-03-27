package com.alainp.githubx.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class UserRemoteKey(
    @PrimaryKey val id: Long = 1,
    val since: Long
)

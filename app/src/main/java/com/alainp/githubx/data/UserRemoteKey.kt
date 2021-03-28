package com.alainp.githubx.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class UserRemoteKey(
    @PrimaryKey val id: Long = 1, // This is a temporary solution. There's no need to create a table for this we could use DatStore instead.
    val since: Long
)

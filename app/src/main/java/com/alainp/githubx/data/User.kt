package com.alainp.githubx.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)

class UserDetail
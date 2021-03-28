package com.alainp.githubx.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)

@Entity(tableName = "userDetail")
data class UserDetail(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("followers_url") val followersUrl: String?,
    @SerializedName("following_url") val followingUrl: String?,
    @SerializedName("gists_url") val gistsUrl: String?,
    @SerializedName("starred_url") val starredUrl: String?,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String?,
    @SerializedName("organizations_url") val organizationsUrl: String?,
    @SerializedName("repos_url") val reposUrl: String?,
    @SerializedName("events_url") val eventsUrl: String?,
    @SerializedName("received_events_url") val received_eventsUrl: String?,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    @SerializedName("twitter_username")
    val twitter: String?,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int,
    @Expose(serialize = false, deserialize = false) val lastUpdated: Long? = null
)
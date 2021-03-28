package com.alainp.githubx.viewmodels

import com.alainp.githubx.data.User

class ListItemUserViewModel(private val user: User) {
    val login: String
        get() = user.login

    val avatarUrl: String
        get() = user.avatarUrl
}
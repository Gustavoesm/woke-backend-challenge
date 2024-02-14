package com.woke.challenge.backend.model.repositories

import com.woke.challenge.backend.model.UserInfo
import com.woke.challenge.backend.model.Username

interface UserInfoRepository {
    fun exists(username: Username): Boolean
    fun get(username: Username): UserInfo
    fun save(username: Username, userInfo: UserInfo)
}

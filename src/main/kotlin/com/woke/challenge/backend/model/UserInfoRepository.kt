package com.woke.challenge.backend.model

interface UserInfoRepository {
    fun exists(username: Username): Boolean
    fun get(username: Username): UserInfo
    fun save(username: Username, userInfo: UserInfo)
}

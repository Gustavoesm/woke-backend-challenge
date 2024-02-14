package com.woke.challenge.backend.services

import com.woke.challenge.backend.model.UserInfo
import com.woke.challenge.backend.model.repositories.UserInfoRepository
import com.woke.challenge.backend.model.exceptions.UserNotFoundException
import com.woke.challenge.backend.model.Username
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserInfoService {
    @Autowired
    lateinit var repository: UserInfoRepository

    fun createIfNotExists(username: Username, userInfo: UserInfo): Boolean {
        if(repository.exists(username)){
            return false
        }

        repository.save(username, userInfo)
        return true
    }

    fun recover(username: Username): UserInfo {
        if(repository.exists(username)){
            return repository.get(username)
        }

        throw UserNotFoundException("User ${username.value} not found!")
    }
}

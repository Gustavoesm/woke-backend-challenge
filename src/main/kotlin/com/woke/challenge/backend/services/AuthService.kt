package com.woke.challenge.backend.services

import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.repositories.CredentialRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService: UserDetailsService {
    @Autowired
    lateinit var repository: CredentialRepository

    fun attemptCreate(credential: Credential): Boolean {
        if(repository.exists(credential.username)){
            return false
        }
        return repository.save(credential)
    }

    fun getIndex(): List<Credential> {
        return repository.devIndex()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findByUsername(username)
    }
}
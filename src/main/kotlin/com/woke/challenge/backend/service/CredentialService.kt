package com.woke.challenge.backend.service

import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.CredentialRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CredentialService {
    @Autowired
    lateinit var repository: CredentialRepository

    fun attemptLogin(credential: Credential): Boolean {
        return repository.exists(credential)
    }

    fun attemptCreate(credential: Credential): Boolean {
        return repository.save(credential)
    }

    fun getIndex(): List<Credential> {
        return repository.devIndex()
    }
}
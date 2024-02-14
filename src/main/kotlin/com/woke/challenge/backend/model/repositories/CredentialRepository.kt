package com.woke.challenge.backend.model.repositories

import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.Username
import org.springframework.security.core.userdetails.UserDetails

interface CredentialRepository {
    fun exists(username: Username): Boolean
    fun save(credential: Credential): Boolean
    fun devIndex(): List<Credential>
    fun findByUsername(username: String): UserDetails
}
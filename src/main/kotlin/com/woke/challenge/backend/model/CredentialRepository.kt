package com.woke.challenge.backend.model


interface CredentialRepository {
    fun save(credential: Credential): Boolean
    fun exists(credential: Credential): Boolean
    fun devIndex(): List<Credential>
}
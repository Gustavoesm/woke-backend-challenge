package com.woke.challenge.backend.infra

data class UserInfoDTO(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val birthDate: String
)

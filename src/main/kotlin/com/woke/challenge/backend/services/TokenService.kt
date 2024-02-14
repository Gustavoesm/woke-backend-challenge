package com.woke.challenge.backend.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.exceptions.TokenGenerationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {

    @Value("\${api.security.token.secret}")
    lateinit var secret: String

    fun generateToken(credential: Credential): String {
        try {
            val algorithm: Algorithm = Algorithm.HMAC256(secret)
            val token = JWT.create()
                .withIssuer("woke-challenge")
                .withSubject(credential.username.value)
                .withExpiresAt(aDayFromNow())
                .sign(algorithm)
            return token
        } catch (err: JWTCreationException) {
            throw TokenGenerationException("Unable to generate auth token.")
        }
    }

    fun validateToken(token: String): String {
        try {
            val algorithm: Algorithm = Algorithm.HMAC256(secret)
            return JWT.require(algorithm)
                .withIssuer("woke-challenge")
                .build()
                .verify(token)
                .subject
        } catch (err: JWTVerificationException) {
            return ""
        }
    }

    private fun aDayFromNow(): Instant {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"))
    }
}
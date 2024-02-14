package com.woke.challenge.backend.controllers

import com.woke.challenge.backend.infra.DTO.AuthTokenDTO
import com.woke.challenge.backend.infra.DTO.CredentialDTO
import com.woke.challenge.backend.infra.DTO.IndexResponseDTO
import com.woke.challenge.backend.infra.ResponseHandler.generateResponse
import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.Password
import com.woke.challenge.backend.model.Username
import com.woke.challenge.backend.services.AuthService
import com.woke.challenge.backend.services.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/auth")
class AuthController {
    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var authManager: AuthenticationManager

    @Autowired
    lateinit var encoder: PasswordEncoder

    @PostMapping("/login")
    fun attemptLogin(@RequestBody request: CredentialDTO): ResponseEntity<Any> {
        try {
            val loginToken = UsernamePasswordAuthenticationToken(request.username, request.password)
            val auth = authManager.authenticate(loginToken)
            val token = tokenService.generateToken(auth.principal as Credential)
            return generateResponse("Login successful.", HttpStatus.OK, AuthTokenDTO(token))
        } catch (err: IllegalArgumentException) {
            return generateResponse("Login failed.", HttpStatus.UNAUTHORIZED, {})
        }
    }

    @PostMapping("/create")
    fun createUser(@RequestBody payload: CredentialDTO): ResponseEntity<Any> {
        return try {
            if (authService.attemptCreate(asCredential(payload))) {
                generateResponse("Account created.", HttpStatus.CREATED, {})
            } else {
                generateResponse("Username already exists.", HttpStatus.BAD_REQUEST, {})
            }
        } catch (err: IllegalArgumentException) {
            generateResponse("Unable to create account.", HttpStatus.BAD_REQUEST, err.message!!)
        }
    }

    @GetMapping("/dev/index")
    fun listUsers(): ResponseEntity<Any> {
        return generateResponse("[DEV] Listing all users:", HttpStatus.OK, format(authService.getIndex()))
    }

    private fun asCredential(payload: CredentialDTO): Credential {
        return Credential(Username(payload.username), Password(encoder.encode(payload.password), true))
    }

    private fun format(index: List<Credential>): Any {
        val indexPayload =
            index.map { credential -> CredentialDTO(credential.username.value, credential.password.value) }
        return IndexResponseDTO(indexPayload)
    }
}

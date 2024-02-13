package com.woke.challenge.backend.infra

import com.woke.challenge.backend.infra.ResponseHandler.generateResponse
import com.woke.challenge.backend.model.Credential
import com.woke.challenge.backend.model.Password
import com.woke.challenge.backend.model.Username
import com.woke.challenge.backend.service.CredentialService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/identity")
class CredentialController {
    @Autowired
    lateinit var service: CredentialService

    @PostMapping("/login")
    fun attemptLogin(@RequestBody payload: CredentialPayload): ResponseEntity<Any> {
        try {
            service.attemptLogin(asCredential(payload))
        } catch (err: IllegalArgumentException) {
            return generateResponse("Login failed.", HttpStatus.UNAUTHORIZED, {})
        }
        return generateResponse("Login successful.", HttpStatus.OK, {})
    }

    @PostMapping("/create")
    fun createUser(@RequestBody payload: CredentialPayload): ResponseEntity<Any> {
        try {
            service.attemptCreate(asCredential(payload))
        } catch (err: IllegalArgumentException) {
            return generateResponse("Unable to create account.", HttpStatus.BAD_REQUEST, err.message!!)
        }
        return generateResponse("Account created.", HttpStatus.CREATED, {})
    }

    @GetMapping("/dev/index")
    fun listUsers(): ResponseEntity<Any> {
        return generateResponse("[DEV] Listing all users:", HttpStatus.OK, format(service.getIndex()))
    }

    private fun asCredential(payload: CredentialPayload): Credential{
        return Credential(Username(payload.username), Password(payload.password))
    }

    private fun format(index: List<Credential>): Any {
        val indexPayload = index.map { credential -> CredentialPayload(credential.username.value, credential.password.value) }
        return IndexResponse(indexPayload)
    }
}

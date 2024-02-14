package com.woke.challenge.backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/consume")
class DummyConsumerController {

    @PostMapping
    fun consume(@RequestBody request: ConsumeDataDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(request)
    }

    data class ConsumeDataDTO(
        val fullName: String, val phone: String, val email: String, val birthDate: String
    )
}
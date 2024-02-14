package com.woke.challenge.backend.controllers

import com.woke.challenge.backend.infra.DTO.UserInfoDTO
import com.woke.challenge.backend.infra.ResponseHandler.generateResponse
import com.woke.challenge.backend.model.*
import com.woke.challenge.backend.model.exceptions.UserNotFoundException
import com.woke.challenge.backend.services.UserInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/{username}")
class UserInfoController {

    @Autowired
    lateinit var service: UserInfoService

    @PostMapping("/info")
    fun create(@PathVariable username: String, @RequestBody request: UserInfoDTO): ResponseEntity<Any> {
        return if (service.createIfNotExists(asUsername(username), asUserInfo(request))) {
            generateResponse("Data set for user.", HttpStatus.CREATED, {})
        } else {
            generateResponse("User already has data set.", HttpStatus.BAD_REQUEST, {})
        }
    }

    @GetMapping("/info")
    fun recover(@PathVariable username: String): ResponseEntity<Any> {
        try {
            val user = service.recover(asUsername(username))
            return generateResponse("Done.", HttpStatus.OK, payloadFrom(user))
        } catch (err: UserNotFoundException) {
            return generateResponse(err.message!!, HttpStatus.NOT_FOUND, {})
        }
    }

    private fun payloadFrom(user: UserInfo): UserInfoDTO {
        return UserInfoDTO(
            user.firstName.value,
            user.lastName.value,
            user.phone.value,
            user.email.value,
            formatter.format(user.birthDate.value)
        )
    }

    private fun asUsername(username: String): Username {
        return Username(username)
    }

    private fun asUserInfo(payload: UserInfoDTO): UserInfo {
        return UserInfo(
            FirstName(payload.firstName),
            LastName(payload.lastName),
            PhoneNumber(payload.phone),
            Email(payload.email),
            BirthDate(formatter.parse(payload.birthDate))
        )
    }

    companion object {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
    }
}
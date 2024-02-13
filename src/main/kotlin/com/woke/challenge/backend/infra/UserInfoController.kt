package com.woke.challenge.backend.infra

import com.woke.challenge.backend.infra.ResponseHandler.generateResponse
import com.woke.challenge.backend.model.*
import com.woke.challenge.backend.service.UserInfoService
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
    fun create(@PathVariable username: String, @RequestBody request: UserInfoPayload): ResponseEntity<Any> {
        service.createIfNotExists(asUsername(username), asUserInfo(request))
        return generateResponse("Data set for user.", HttpStatus.CREATED, {})
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

    private fun payloadFrom(user: UserInfo): UserInfoPayload {
        return UserInfoPayload(
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

    private fun asUserInfo(payload: UserInfoPayload): UserInfo {
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
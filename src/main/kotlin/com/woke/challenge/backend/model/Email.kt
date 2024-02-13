package com.woke.challenge.backend.model

class Email(val value: String) {
    init {
        require(value.matches(emailPattern)) { ERR_EMAIL_PATTERN}
        require(value.length <= 32) { ERR_EMAIL_TOO_LONG}
    }

    companion object {
        val emailPattern = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        const val ERR_EMAIL_PATTERN = "Invalid email!"
        const val ERR_EMAIL_TOO_LONG = "Emails must contain a max of 128 characters!"
    }
}

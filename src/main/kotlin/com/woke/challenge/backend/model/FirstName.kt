package com.woke.challenge.backend.model

class FirstName(val value: String) {
    init {
        require(value.length <= 32) { ERR_TOO_LONG}
        require(value.all { it.isLetter() || it == ' ' }) { ERR_INVALID }
    }

    companion object {
        const val ERR_TOO_LONG = "First names must contain a max of 32 digits!"
        const val ERR_INVALID = "Names must contain only letters or spaces!"
    }
}

package com.woke.challenge.backend.model

class LastName(val value: String) {
    init {
        require(value.length <= 96) { ERR_TOO_LONG}
        require(value.all { it.isLetter() || it == ' ' }) { ERR_INVALID }
    }

    companion object {
        const val ERR_TOO_LONG = "Last names must contain a max of 96 digits!"
        const val ERR_INVALID = "Names must contain only letters or spaces!"
    }
}

package com.woke.challenge.backend.model

class Username(val value: String) {
    init {
        require(value.length >= 5) { ERR_LEN }
        require(value.all { it.isLetterOrDigit() }) { ERR_ALPHA}
    }

    companion object{
        const val ERR_LEN = "Username must have at least five characters!"
        const val ERR_ALPHA = "Username must contain only letters or numbers!"
    }
}
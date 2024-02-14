package com.woke.challenge.backend.model

class Password(val value: String, encoded: Boolean = false) {
    init {
        if(!encoded){
            require(value.length >= 8) { ERR_LEN }
            require(value.none { it.isWhitespace() }) { ERR_WHITESPACE }
            require(value.any { it.isDigit() }) { ERR_DIGIT }
            require(value.any { it.isUpperCase() }) { ERR_UPPER }
            require(value.any { !it.isLetterOrDigit() }) { ERR_SPECIAL }
        }
    }

    companion object{
        const val ERR_LEN = "Password must have at least eight characters!"
        const val ERR_WHITESPACE = "Password must not contain whitespace!"
        const val ERR_DIGIT = "Password must contain at least one digit!"
        const val ERR_UPPER = "Password must have at least one uppercase letter!"
        const val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"
    }
}

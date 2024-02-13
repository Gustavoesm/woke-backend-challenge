package com.woke.challenge.backend.model

class PhoneNumber(val value: String) {
    init {
        require(value.matches(phonePattern)) { ERR_PHONE_PATTERN }
    }

    companion object {
        val phonePattern = Regex("^(\\+[1-9]{2})? ?\\(?[1-9]{2}\\)? ?(?:[2-8]|9[0-9])[0-9]{3}-?[0-9]{4}$")
        const val ERR_PHONE_PATTERN = "Phone numbers must follow international patterns!\n" +
                "e.g. \"+55 (11) 98765-4321\""
    }
}

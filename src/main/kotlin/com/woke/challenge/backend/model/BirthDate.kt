package com.woke.challenge.backend.model

import java.util.Date

class BirthDate(val value: Date) {
    init {
        require(value.before(Date())) { ERR_BIRTH_BEFORE_CURRENT_TIME }
    }

    companion object {
        const val ERR_BIRTH_BEFORE_CURRENT_TIME = "Birth Dates must be before current time!"
    }
}

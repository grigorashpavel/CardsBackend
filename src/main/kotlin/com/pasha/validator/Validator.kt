package com.pasha.validator



internal class Validator private constructor() {

    companion object {
        fun isEmailValid(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"

            return emailRegex.toRegex().matches(email)
        }

        fun isPasswordValid(password: String): Boolean = password.length in 7..20
    }
}
package com.pasha.validator


//private sealed class ValidationException(message: String) : RuntimeException(message) {
//    class PasswordValidException(message: String) : ValidationException(message)
//    class EmailValidException(message: String) : ValidationException(message)
//}

internal class Validator private constructor() {

    companion object {
        fun isEmailValid(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"

            return emailRegex.toRegex().matches(email)
        }

        fun isPasswordValid(password: String): Boolean = password.length in 7..20
    }
}
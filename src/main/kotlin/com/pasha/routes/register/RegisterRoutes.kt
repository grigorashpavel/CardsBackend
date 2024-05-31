package com.pasha.routes.register


import com.pasha.models.users.CredentialsDto
import com.pasha.repositories.tokens.TokensRepository
import com.pasha.repositories.users.UsersRepository
import com.pasha.util.Constants
import com.pasha.validator.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


private const val ERROR_MSG_VALID = "Недопустимый логин или пароль!"
private const val ERROR_MSG_USR_EXIST = "Такой пользователь уже существует."

fun Route.registerRoute(
    usersRepository: UsersRepository,
    tokensRepository: TokensRepository
) {
    post("/auth/register") {
        val credentials = call.receive<CredentialsDto>()
        if (!Validator.isEmailValid(credentials.email) || !Validator.isPasswordValid(credentials.password)) {
            call.respond(HttpStatusCode.BadRequest, ERROR_MSG_VALID)
            return@post
        }

        if (!usersRepository.isUserExist(credentials.email)) {
            usersRepository.addUserToDb(credentials)

            val tokens = tokensRepository.generateTokens(credentials)

            tokensRepository.registerTokens(tokens, credentials.deviceId)

            call.respond(
                HttpStatusCode.Created,
                hashMapOf(
                    Constants.ACCESS_TOKEN_TAG to tokens.accessToken.value,
                    Constants.REFRESH_TOKEN_TAG to tokens.refreshToken.value
                )
            )
        } else {
            call.respond(HttpStatusCode.BadRequest, ERROR_MSG_USR_EXIST)
        }
    }
}
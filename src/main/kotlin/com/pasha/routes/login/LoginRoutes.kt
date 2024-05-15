package com.pasha.routes.login


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


fun Route.loginRoute(
    usersRepository: UsersRepository,
    tokensRepository: TokensRepository
) {
    post("/login") {
        val credentials = call.receive<CredentialsDto>()
        if (!Validator.isEmailValid(credentials.email) || !Validator.isPasswordValid(credentials.password)) {
            call.respond(HttpStatusCode.Unauthorized, "Bad Auth Data")
            return@post
        }

        if (usersRepository.isUserExist(credentials.email) && usersRepository.checkUserCredentials(credentials)) {
            val tokens = tokensRepository.generateTokens(credentials)
            tokensRepository.registerTokens(tokens, credentials.deviceId)

            call.respond(
                HttpStatusCode.OK,
                hashMapOf(
                    Constants.ACCESS_TOKEN_TAG to tokens.accessToken.value,
                    Constants.REFRESH_TOKEN_TAG to tokens.refreshToken.value
                )
            )
        } else call.respond(HttpStatusCode.Unauthorized, "Bad Auth Data")
    }
}
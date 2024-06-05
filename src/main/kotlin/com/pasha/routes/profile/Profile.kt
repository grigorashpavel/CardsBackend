package com.pasha.routes.profile

import com.pasha.repositories.users.UsersRepository
import com.pasha.validator.TokenExtractor
import io.ktor.client.engine.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.profile(
    usersRepository: UsersRepository
) {
    authenticate("auth-jwt") {
        get("/api/v1/profile") {
            println(call.request.headers[HttpHeaders.Authorization])
            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject
            val user = usersRepository.getUser(email)
            if (user != null) {
                call.respond(
                    HttpStatusCode.OK,
                    hashMapOf(
                        "headerBackgroundPath" to "/api/v1/backgrounds/random",
                        "email" to user.email,
                        "username" to user.username,
                        "avatarPath" to user.avatarPath
                    )
                )
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/api/v1/profile") {

        }
    }
}
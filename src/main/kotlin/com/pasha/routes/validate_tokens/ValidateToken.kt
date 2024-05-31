package com.pasha.routes.validate_tokens

import com.pasha.models.users.CredentialsDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.validateTokensRoute() {
    authenticate("jwt") {
        get("/") {
            call.respond(HttpStatusCode.OK, "")
        }
    }
}
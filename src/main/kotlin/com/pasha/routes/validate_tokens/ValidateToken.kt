package com.pasha.routes.validate_tokens

import com.pasha.plugins.Security
import com.pasha.routes.Routes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.validateTokensRoute() {
    authenticate(Security.JWT.value) {
        get("${Routes.SESSIONS}/${Routes.ValidateToken}") {
            call.respond(HttpStatusCode.OK)
        }
    }
}
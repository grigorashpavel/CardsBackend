package com.pasha.plugins

import com.auth0.jwt.exceptions.IncorrectClaimException
import com.pasha.services.tokens.TokenException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<TokenException.InvalidTokenException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message.toString())
        }
        exception<IncorrectClaimException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message.toString())
        }
    }
}
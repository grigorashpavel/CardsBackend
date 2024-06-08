package com.pasha.routes.active_sessions

import com.pasha.plugins.Security
import com.pasha.repositories.users.UsersRepository
import com.pasha.routes.Routes
import com.pasha.util.TokenExtractor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.activeSessionsRoutes(
    usersRepository: UsersRepository
) {
    authenticate(Security.AUTH_JWT.value) {
        get("${Routes.SESSIONS}/${Routes.Active}") {
            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject

            if (email.isNullOrEmpty()) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val activeSessions = usersRepository.getActiveSessions(email)
            if (activeSessions == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            println(activeSessions)
            call.respond(
                HttpStatusCode.OK,
                hashMapOf("activeSessions" to activeSessions)
            )
        }
    }
}
package com.pasha.routes.exits

import com.pasha.models.users.CredentialsDto
import com.pasha.repositories.tokens.TokensRepository
import com.pasha.routes.Routes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.stopCurrentSession(
    tokensRepository: TokensRepository
) {
    post("${Routes.SESSIONS}/${Routes.StopCurrent}") {
        val deviceId = call.receive<CredentialsDto>().deviceId
        tokensRepository.revokeCurrentDeviceTokens(deviceId)

        call.respond(HttpStatusCode.OK)
    }
}
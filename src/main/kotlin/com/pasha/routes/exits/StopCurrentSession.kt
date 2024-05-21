package com.pasha.routes.exits

import com.pasha.models.users.CredentialsDto
import com.pasha.repositories.tokens.TokensRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.stopCurrentSession(
    tokensRepository: TokensRepository
) {
    post("/sessions/stop-current") {
        val deviceId = call.receive<CredentialsDto>().deviceId
        tokensRepository.revokeCurrentDeviceTokens(deviceId)

        call.respond("Mb stopped current session")
    }
}
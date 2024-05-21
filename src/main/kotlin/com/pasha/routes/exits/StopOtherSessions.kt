package com.pasha.routes.exits


import com.pasha.models.users.CredentialsDto
import com.pasha.repositories.tokens.TokensRepository
import com.pasha.repositories.users.UsersRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.stopOtherSessions(
    usersRepository: UsersRepository,
    tokensRepository: TokensRepository
) {
    post("/sessions/stop-other") {
        val credentials = call.receive<CredentialsDto>()
        val deviceId = credentials.deviceId
        val email = credentials.email
        val userDevicesId = usersRepository.getDevicesIdByUser(email)

        val otherDevicesId = userDevicesId.toMutableList().apply { remove(deviceId) }
        tokensRepository.revokeTokensByDevicesId(otherDevicesId)

        call.respond("Mb stopped other sessions")
    }
}
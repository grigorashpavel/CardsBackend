package com.pasha.routes.exits


import com.pasha.models.users.CredentialsDto
import com.pasha.repositories.tokens.TokensRepository
import com.pasha.repositories.users.UsersRepository
import com.pasha.routes.Routes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.stopOtherSessions(
    usersRepository: UsersRepository,
    tokensRepository: TokensRepository
) {
    post("${Routes.SESSIONS}/${Routes.StopOther}") {
        val credentials = call.receive<CredentialsDto>()
        val deviceId = credentials.deviceId
        val email = credentials.email
        val userDevicesId = usersRepository.getDevicesIdByUser(email)

        val otherDevicesId = userDevicesId.toMutableList().apply { remove(deviceId) }
        tokensRepository.revokeTokensByDevicesId(otherDevicesId)

        call.respond(HttpStatusCode.OK)
    }
}
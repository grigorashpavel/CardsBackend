package com.pasha.routes.tokens_refresh

import com.pasha.models.users.CredentialsDto
import com.pasha.plugins.Security
import com.pasha.repositories.tokens.TokensRepository
import com.pasha.routes.Routes
import com.pasha.util.Constants
import com.pasha.util.TokenExtractor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.refreshTokensRoute(tokensRepository: TokensRepository) {
    authenticate(Security.REFRESH_JWT.value) {
        post("${Routes.SESSIONS}/${Routes.ExtendCurrent}") {
            println("Authorization-${call.request.headers[HttpHeaders.Authorization]}")
            val token = TokenExtractor.extractToken(call.request)
            val deviceId = call.receive<CredentialsDto>().deviceId

            token?.let {
                val payload = TokenExtractor.extractPayload(token)

                tokensRepository.revokeTokensByDevicesId(listOf(deviceId))

                val email = payload!!.subject
                val tokens = tokensRepository.generateTokens(CredentialsDto(email, "", deviceId, ""))

                tokensRepository.registerTokens(tokens, deviceId)

                call.respond(
                    HttpStatusCode.OK,
                    hashMapOf(
                        Constants.ACCESS_TOKEN_TAG to tokens.accessToken.value,
                        Constants.REFRESH_TOKEN_TAG to tokens.refreshToken.value
                    )
                )
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }
}
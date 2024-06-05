package com.pasha.routes.tokens_refresh

import com.auth0.jwt.impl.JWTParser
import com.pasha.models.users.CredentialsDto
import com.pasha.repositories.tokens.TokensRepository
import com.pasha.util.Constants
import com.pasha.validator.TokenExtractor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
fun Route.refreshTokensRoute(tokensRepository: TokensRepository) {
    authenticate("refresh-jwt") {
        post("/sessions/extend-current") {
            println("Authorization-${call.request.headers[HttpHeaders.Authorization]}")
            val token = TokenExtractor.extractToken(call.request)
            val deviceId = call.receive<CredentialsDto>().deviceId

            token?.let {
                //val (header, payloadEncode, signature) = token.split('.')
                //JWTParser().parsePayload(Base64.decode(payloadEncode).decodeToString())
                val payload = TokenExtractor.extractPayload(token)

                tokensRepository.revokeTokensByDevicesId(listOf(deviceId))

                val email = payload!!.subject
                val tokens = tokensRepository.generateTokens(CredentialsDto(email, "", deviceId))

                tokensRepository.registerTokens(tokens, deviceId)

                call.respond(
                    HttpStatusCode.OK,
                    hashMapOf(
                        Constants.ACCESS_TOKEN_TAG to tokens.accessToken.value,
                        Constants.REFRESH_TOKEN_TAG to tokens.refreshToken.value
                    )
                )
            } ?: call.respond(HttpStatusCode.BadRequest, "error" to "Header not contained refresh-token to refresh jwt")
        }
    }
}
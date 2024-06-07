package com.pasha.plugins

import com.pasha.repositories.tokens.TokensRepository
import com.pasha.repositories.users.UsersRepository
import com.pasha.services.tokens.TokenType
import com.pasha.services.tokens.TokensService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

enum class Security(val value: String) {
    AUTH_JWT("auth-jwt"), REFRESH_JWT("refresh-jwt"), JWT("jwt")
}

fun Application.configureSecurity() {
    val jwtService: TokensService by inject()
    val usersRepository: UsersRepository by inject()
    val tokensRepository: TokensRepository by inject()

    suspend fun isTokenCorrupted(token: JWTCredential): Boolean {
        val isTokenRevoked = tokensRepository.isTokenRevoked(token.jwtId!!)
        val isUserExist = usersRepository.isUserExist(token.payload.subject)

        val tokenType = token.getClaim("token_type", String::class)

        if (isTokenRevoked && tokenType != TokenType.ACCESS_TOKEN.value) {
            val ids = usersRepository.getDevicesIdByUser(token.subject!!)
            tokensRepository.revokeTokensByDevicesId(ids)
        }

        return isTokenRevoked || !isUserExist
    }

    authentication {
        jwt(Security.AUTH_JWT.value) {
            realm = jwtService.realm
            verifier(jwtService.verifyJWT(TokenType.ACCESS_TOKEN))
            validate { token ->
                if (!isTokenCorrupted(token)) jwtService.validateToken(token) else null
            }
            challenge { _, realm ->
                setupResponseHeaders(call, realm)
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        jwt(Security.REFRESH_JWT.value) {
            realm = jwtService.realm
            verifier(jwtService.verifyJWT(TokenType.REFRESH_TOKEN))
            validate { token ->
                if (!isTokenCorrupted(token)) jwtService.validateToken(token) else null
            }
            challenge { _, realm ->
                setupResponseHeaders(call, realm)
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        jwt(Security.JWT.value) {
            realm = jwtService.realm
            verifier(jwtService.verifyJWT())
            validate { token ->
                if (!isTokenCorrupted(token)) jwtService.validateToken(token) else null
            }
            challenge { _, realm ->
                setupResponseHeaders(call, realm)
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

private fun setupResponseHeaders(call: ApplicationCall, realm: String) {
    call.response.apply {
        val authHeader = "Bearer realm=\"$realm\""
        headers.append(HttpHeaders.WWWAuthenticate, authHeader)
        headers.append("error", "invalid_token")
        headers.append("error_description", "The access token is expired or being stolen")
    }
}
package com.pasha.services.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.IncorrectClaimException
import com.pasha.config.AppConfig
import com.pasha.models.users.CredentialsDto
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


sealed class TokenException(message: String) : RuntimeException(message) {
    class InvalidTokenException(message: String) : TokenException(message)
}

enum class TokenType(val value: String) {
    REFRESH_TOKEN("refresh"),
    ACCESS_TOKEN("access")
}

sealed class Token {
    data class Access(val id: String, val value: String, val expireTime: Date) : Token()
    data class Refresh(val id: String, val value: String, val expireTime: Date) : Token()
}

data class Tokens(
    val accessToken: Token.Access,
    val refreshToken: Token.Refresh
)

@Single
class TokensService(private val config: AppConfig) {
    private val audience by lazy {
        config.appConfig.property("jwt.audience").getString()
    }
    private val issuer by lazy {
        config.appConfig.property("jwt.issuer").getString()
    }
    val realm by lazy {
        config.appConfig.property("jwt.realm").getString()
    }
    private val secret by lazy {
        config.appConfig.property("jwt.secret").getString()
    }

    private val TYPE_TAG = "token_type"

    private fun generateJWTs(email: String, tokenType: String, expireTime: Date, tokenId: String): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(expireTime)
            .withJWTId(tokenId)
            .withClaim(TYPE_TAG, tokenType)
            .withSubject(email)
            .sign(Algorithm.HMAC256(secret))
    }

    fun generateTokens(credentials: CredentialsDto): Tokens {
        val zeroZone = ZoneOffset.UTC
        //val accessExpireDate = Date.from(LocalDateTime.now().plusMinutes(3).toInstant(zeroZone))
        val accessExpireDate = Date.from(LocalDateTime.now().plusHours(3).toInstant(zeroZone))
        val refreshExpireDate = Date.from(LocalDateTime.now().plusDays(7).toInstant(zeroZone))

        val accessId = UUID.randomUUID().toString()
        val refreshId = UUID.randomUUID().toString()

        val accessToken = generateJWTs(credentials.email, TokenType.ACCESS_TOKEN.value, accessExpireDate, accessId)
        val refreshToken = generateJWTs(credentials.email, TokenType.REFRESH_TOKEN.value, refreshExpireDate, refreshId)

        return Tokens(
            Token.Access(accessId, accessToken, accessExpireDate),
            Token.Refresh(refreshId, refreshToken, refreshExpireDate)
        )
    }

    fun verifyJWT(tokenType: TokenType): JWTVerifier {
        val result = try {
            JWT.require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim(TYPE_TAG, tokenType.value)
                .build()
        } catch (e: Exception) {
            throw TokenException.InvalidTokenException("Token incorrect")
        } catch (claimException: IncorrectClaimException) {
            throw TokenException.InvalidTokenException("Token type incorrect")
        }
        return result
    }

    fun validateToken(token: JWTCredential): Principal? {
        return if (token.isNotExpired()) JWTPrincipal(token.payload) else null
    }

    private fun JWTCredential.isNotExpired() = payload.expiresAt.time > System.currentTimeMillis()
}
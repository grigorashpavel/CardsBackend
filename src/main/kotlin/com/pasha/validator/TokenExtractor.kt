package com.pasha.validator

import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.Payload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TokenExtractor {
    companion object {
        fun extractToken(request: ApplicationRequest): String? =
            request.headers[HttpHeaders.Authorization]?.split(' ')?.get(1)

        @OptIn(ExperimentalEncodingApi::class)
        fun extractPayload(token: String?): Payload? {
            if (token == null) return null

            val (header, payloadEncode, signature) = token.split('.')
            return JWTParser().parsePayload(Base64.decode(payloadEncode).decodeToString())
        }
    }
}
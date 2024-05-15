package com.pasha.repositories.tokens

import com.pasha.models.users.CredentialsDto
import com.pasha.services.tokens.Tokens

interface TokensRepository {
    fun generateTokens(credentials: CredentialsDto): Tokens
    suspend fun registerTokens(tokens: Tokens, deviceId: String)
    suspend fun revokeCurrentDeviceTokens(deviceId: String)
    suspend fun revokeTokensByDevicesId(devicesId: List<String>)
    suspend fun revokeTokenById(tokenId: String)

    suspend fun isTokenRevoked(tokenId: String): Boolean
}
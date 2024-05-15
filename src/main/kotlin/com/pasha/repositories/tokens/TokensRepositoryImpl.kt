package com.pasha.repositories.tokens

import com.pasha.database.entities.TokenEntity
import com.pasha.models.users.CredentialsDto
import com.pasha.services.database.DatabaseService
import com.pasha.services.tokens.TokensService
import org.koin.core.annotation.Single
import java.util.*


private typealias ModelTokens = com.pasha.services.tokens.Tokens
private typealias TableTokens = com.pasha.database.entities.Tokens

@Single
class TokensRepositoryImpl(
    private val tokensService: TokensService,
    private val databaseService: DatabaseService
) : TokensRepository {
    override fun generateTokens(credentials: CredentialsDto) = tokensService.generateTokens(credentials)

    override suspend fun registerTokens(tokens: ModelTokens, deviceId: String) {
        databaseService.dbQuery {
            TokenEntity.new(UUID.fromString(tokens.accessToken.id)) {
                isRevoked = false
                expireTime = tokens.accessToken.expireTime.time
                this.deviceId = deviceId
            }
            TokenEntity.new(UUID.fromString(tokens.refreshToken.id)) {
                isRevoked = false
                expireTime = tokens.refreshToken.expireTime.time
                this.deviceId = deviceId
            }
        }
    }

    override suspend fun revokeCurrentDeviceTokens(deviceId: String) {
        databaseService.dbQuery {
            val curDeviceTokens = TokenEntity.find { TableTokens.deviceId eq deviceId }

            curDeviceTokens.forEach { entry ->
                entry.isRevoked = true
            }
        }
    }

    override suspend fun revokeTokensByDevicesId(devicesId: List<String>) {
        databaseService.dbQuery {
            devicesId.forEach { deviceId -> revokeCurrentDeviceTokens(deviceId) }
        }
    }

    override suspend fun revokeTokenById(tokenId: String) {
        databaseService.dbQuery {
            TokenEntity.find { TableTokens.id eq UUID.fromString(tokenId) }
                .first()
                .isRevoked = true
        }
    }

    override suspend fun isTokenRevoked(tokenId: String): Boolean = databaseService.dbQuery {
        TokenEntity.find { TableTokens.id eq UUID.fromString(tokenId) }
            .first()
            .isRevoked
    }
}
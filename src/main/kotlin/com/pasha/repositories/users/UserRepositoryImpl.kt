package com.pasha.repositories.users

import com.pasha.database.entities.*
import com.pasha.models.users.CredentialsDto
import com.pasha.services.database.DatabaseService
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Singleton
import java.util.UUID

@Singleton
class UserRepositoryImpl(
    private val databaseService: DatabaseService
) : UsersRepository {
    override suspend fun isUserExist(email: String): Boolean = databaseService.dbQuery {
        EmailEntity.find { Emails.email eq email }.count() == 1L
    }

    override suspend fun addUserToDb(credentials: CredentialsDto) {
        databaseService.dbQuery {
            val username = getRandomUsername()

            val userId = UUID.randomUUID()
            UserEntity.new(userId) {
                this.username = username
                password = credentials.password
                phoneNumber = username
            }

            EmailEntity.new {
                email = credentials.email
                this.userId = userId
            }

            DeviceEntity.new {
                deviceId = credentials.deviceId
                this.userId = userId
            }
        }
    }

    override suspend fun checkUserCredentials(user: CredentialsDto) = databaseService.dbQuery {
        val userId = EmailEntity.find { Emails.email eq user.email }.firstOrNull()?.userId
        val isCorrectCredentials = if (userId != null) {
            UserEntity.find { (Users.id eq userId) and (Users.password eq user.password) }.count() == 1L
        } else false

        return@dbQuery isCorrectCredentials
    }

    override suspend fun getDevicesIdByUser(email: String): List<String> = databaseService.dbQuery {
        val userId = EmailEntity.find { Emails.email eq email }.first().userId

        val deviceEntries = DeviceEntity.find { Devices.userId eq userId }

        deviceEntries.map { it.deviceId }
    }

    private fun getRandomUsername(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        val usernameBuilder = StringBuilder()
        usernameBuilder.append("user")
        repeat(14) { usernameBuilder.append(allowedChars.random()) }

        return usernameBuilder.toString()
    }
}
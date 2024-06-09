package com.pasha.repositories.users

import com.pasha.database.entities.*
import com.pasha.models.users.CredentialsDto
import com.pasha.models.Device
import com.pasha.models.users.User
import com.pasha.services.database.DatabaseService
import org.jetbrains.exposed.sql.and
import org.koin.core.annotation.Single
import java.util.*

@Single
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
            UserEntity.new(id = userId) {
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
                deviceName = credentials.deviceName
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

    override suspend fun getUser(email: String?): User? {
        if (email == null) return null

        return databaseService.dbQuery {
            val userId = EmailEntity.find { Emails.email eq email }.firstOrNull()?.userId
            val user = UserEntity.find { Users.id eq userId }.firstOrNull()

            return@dbQuery if (user != null) {
                User(
                    email = email,
                    username = user.username,
                    avatarPath = user.avatarPath,
                    password = null
                )
            } else null
        }
    }

    override suspend fun updateUsername(email: String, username: String): Boolean {
        return databaseService.dbQuery {
            val isUnique = UserEntity.find { Users.username eq username }.count() == 0L

            val userId = EmailEntity.find { Emails.email eq email }.firstOrNull()?.userId
            return@dbQuery if (userId != null && username.isNotEmpty() && isUnique) {
                val user = UserEntity[userId]
                user.username = username

                true

            } else false
        }
    }

    override suspend fun updateAvatarPath(email: String) {
        databaseService.dbQuery {
            val userId = EmailEntity.find { Emails.email eq email }.first().userId
            val user = UserEntity[userId]

            user.avatarPath = "/api/v1/profile/avatar"
        }
    }

    override suspend fun getActiveSessions(email: String): List<Device>? {
        return databaseService.dbQuery {
            val userId = EmailEntity.find { Emails.email eq email }.first().userId
            val userDevices = DeviceEntity.find { Devices.userId eq userId }

            val activeDevices = mutableListOf<Device>()
            for (device in userDevices) {
                val deviceTokens = TokenEntity.find { (Tokens.deviceId eq device.deviceId) }
                for (token in deviceTokens) {
                    if (token.isRevoked.not()) {
                        val dev = Device(device.deviceId, device.deviceName)
                        activeDevices.add(dev)
                        break
                    }
                }
            }

            return@dbQuery if (activeDevices.size > 0) {
                activeDevices
            } else {
                null
            }
        }
    }

    override suspend fun addDeviceIfNotExist(deviceId: String, deviceName: String, email: String) {
        databaseService.dbQuery {
            val notExist = DeviceEntity.find { Devices.deviceId eq deviceId }.count() == 0L
            if (notExist) {
                val userId = EmailEntity.find { Emails.email eq email }.first().userId
                DeviceEntity.new {
                    this.userId = userId
                    this.deviceId = deviceId
                    this.deviceName = deviceName
                }
            }
        }
    }

    override suspend fun getEmailId(email: String): UUID {
        return databaseService.dbQuery {
            EmailEntity.find { Emails.email eq email }.first().emailId
        }
    }

    override suspend fun getUserId(email: String): UUID {
        return databaseService.dbQuery {
            EmailEntity.find { Emails.email eq email }.first().userId
        }
    }

    private fun getRandomUsername(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        val usernameBuilder = StringBuilder()
        usernameBuilder.append("user")
        repeat(14) { usernameBuilder.append(allowedChars.random()) }

        return usernameBuilder.toString()
    }
}
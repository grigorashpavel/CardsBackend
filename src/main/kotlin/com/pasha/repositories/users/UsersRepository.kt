package com.pasha.repositories.users

import com.pasha.models.users.CredentialsDto
import com.pasha.models.Device
import com.pasha.models.users.User
import java.util.UUID

interface UsersRepository {
    suspend fun isUserExist(email: String): Boolean
    suspend fun addUserToDb(credentials: CredentialsDto)
    suspend fun checkUserCredentials(user: CredentialsDto): Boolean
    suspend fun getDevicesIdByUser(email: String): List<String>
    suspend fun getUser(email: String?): User?
    suspend fun updateUsername(email: String, username: String): Boolean
    suspend fun updateAvatarPath(email: String)
    suspend fun getActiveSessions(email: String): List<Device>?
    suspend fun addDeviceIfNotExist(deviceId: String, deviceName: String, email: String)
    suspend fun getEmailId(email: String): UUID
    suspend fun getUserId(email: String): UUID
}
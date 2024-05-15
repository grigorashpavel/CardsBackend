package com.pasha.repositories.users

import com.pasha.models.users.CredentialsDto
import java.util.UUID

interface UsersRepository {
    suspend fun isUserExist(email: String): Boolean
    suspend fun addUserToDb(credentials: CredentialsDto)
    suspend fun checkUserCredentials(user: CredentialsDto): Boolean
    suspend fun getDevicesIdByUser(email: String): List<String>
}
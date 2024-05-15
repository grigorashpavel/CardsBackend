package com.pasha.models.users

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(
    val email: String,
    val password: String,
    val deviceId: String
)
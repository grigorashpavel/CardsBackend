package com.pasha.models.users

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val headerBackgroundPath: String,
    val username: String,
    val email: String,
    val avatarPath: String
)

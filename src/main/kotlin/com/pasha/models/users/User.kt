package com.pasha.models.users


import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    var username: String,
    var avatarPath: String?,
    var password: String?
)
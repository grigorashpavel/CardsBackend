package com.pasha.models.users

data class User(
    val email: String,
    var username: String,
    var avatarPath: String?,
    var password: String?
)
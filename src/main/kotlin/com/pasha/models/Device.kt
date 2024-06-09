package com.pasha.models

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val deviceId: String,
    val deviceName: String
)

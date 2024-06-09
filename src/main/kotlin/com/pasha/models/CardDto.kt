package com.pasha.models

import java.util.UUID

import kotlinx.serialization.Serializable

@Serializable
data class CardDto(
    val cardName: String,
    val cardId: String,
    val creationTime: String
)

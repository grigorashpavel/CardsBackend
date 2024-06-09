package com.pasha.repositories.cards

import com.pasha.models.CardDto
import java.util.UUID

interface CardsRepository {
    suspend fun cardAlreadyExist(cardName: String?, emailId: UUID): Boolean
    suspend fun saveCard(cardName: String, cardPath: String, userId: UUID, emailId: UUID, creationTime: String): Boolean

    suspend fun getAllCards(emailId: UUID): List<CardDto>
    suspend fun deleteCard(cardId: UUID): Boolean
    suspend fun getCardPath(cardId: UUID): String
    suspend fun getCardTime(cardId: UUID): String

    suspend fun findSimilarCards(queryName: String): List<CardDto>
}
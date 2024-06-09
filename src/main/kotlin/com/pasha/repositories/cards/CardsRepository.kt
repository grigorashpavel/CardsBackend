package com.pasha.repositories.cards

import java.util.UUID

interface CardsRepository {
    suspend fun cardAlreadyExist(cardName: String?, emailId: UUID): Boolean
    suspend fun saveCard(cardName: String, cardPath: String, userId: UUID, emailId: UUID): Boolean
}
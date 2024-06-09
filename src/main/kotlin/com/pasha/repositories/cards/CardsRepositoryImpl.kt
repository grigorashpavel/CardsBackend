package com.pasha.repositories.cards

import com.pasha.database.entities.CardEntity
import com.pasha.database.entities.Cards
import com.pasha.services.database.DatabaseService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.koin.core.annotation.Single
import java.util.*

@Single
class CardsRepositoryImpl(
    private val databaseService: DatabaseService
) : CardsRepository {
    override suspend fun cardAlreadyExist(cardName: String?, emailId: UUID): Boolean {
        if (cardName == null) return false

        return databaseService.dbQuery {
            val query = (Cards.emailId eq emailId) and (Cards.cardName eq cardName)
            CardEntity.find(query).count() >= 1L
        }
    }

    override suspend fun saveCard(cardName: String, cardPath: String, userId: UUID, emailId: UUID): Boolean {
        databaseService.dbQuery {
            CardEntity.new {
                this.cardName = cardName
                this.cardPath = cardPath
                this.userId = userId
                this.emailId = emailId
            }
        }

        return true
    }
}
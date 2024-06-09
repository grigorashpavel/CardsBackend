package com.pasha.repositories.cards

import com.pasha.database.entities.CardEntity
import com.pasha.database.entities.Cards
import com.pasha.file_manager.FileManager
import com.pasha.models.CardDto
import com.pasha.services.database.DatabaseService
import com.pasha.util.SearchUtil.Companion.calculateNameSimilarity
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

    override suspend fun saveCard(
        cardName: String, cardPath: String, userId: UUID, emailId: UUID, creationTime: String
    ): Boolean {
        databaseService.dbQuery {
            CardEntity.new {
                this.cardName = cardName
                this.cardPath = cardPath
                this.userId = userId
                this.emailId = emailId
                this.cardCreateTime = creationTime
            }
        }

        return true
    }

    override suspend fun getAllCards(emailId: UUID): List<CardDto> {
        return databaseService.dbQuery {
            CardEntity.find { Cards.emailId eq emailId }.map { card ->
                CardDto(cardName = card.cardName, cardId = card.cardId.toString(), creationTime = card.cardCreateTime)
            }
        }
    }

    override suspend fun deleteCard(cardId: UUID): Boolean {
        return databaseService.dbQuery {
            val card = CardEntity.find { Cards.id eq cardId }.first()
            val isRemoved = FileManager.removeFileByPath("${card.cardPath}${card.cardName}")
            if (isRemoved) {
                card.delete()
                return@dbQuery true
            } else {
                return@dbQuery false
            }
        }
    }

    override suspend fun getCardPath(cardId: UUID): String {
        return databaseService.dbQuery {
            val card = CardEntity.find { Cards.id eq cardId }.first()
            return@dbQuery "${card.cardPath}${card.cardName}"
        }
    }

    override suspend fun getCardTime(cardId: UUID): String {
        return databaseService.dbQuery {
            val card = CardEntity.find { Cards.id eq cardId }.first()
            return@dbQuery card.cardCreateTime
        }
    }

    override suspend fun findSimilarCards(queryName: String): List<CardDto> {
        return databaseService.dbQuery {
            val allCards = CardEntity.all().toList()
            val threshold = queryName.length


            val similarCards = mutableListOf<CardDto>()

            for (card in allCards) {
                val similarity = calculateNameSimilarity(card.cardName, queryName)
                if (similarity >= threshold) {
                    similarCards.add(CardDto(card.cardName, card.cardId.toString(), card.cardCreateTime))
                }
            }

            similarCards
        }
    }
}
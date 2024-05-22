package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.util.UUID

object Cards : UUIDTable() {
    val cardName: Column<String> = varchar(ColumnData.COL_CARD_NAME_NAME, ColumnData.CARD_NAME_LEN)
    val cardDocument: Column<ExposedBlob> = blob(ColumnData.COL_CARD_DOCUMENT_NAME)
    val userId: Column<UUID> = uuid(ColumnData.COL_USERNAME_NAME)
    val emailId: Column<UUID> = uuid(ColumnData.COL_EMAIL_ID_NAME)
}

class CardEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CardEntity>(Cards)

    val cardId: UUID get() = id.value
    var cardName by Cards.cardName
    var document by Cards.cardDocument
    var userId by Cards.userId
    var emailId by Cards.emailId
}


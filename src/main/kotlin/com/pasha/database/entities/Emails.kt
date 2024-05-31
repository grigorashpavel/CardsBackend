package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object Emails : UUIDTable() {
    val email: Column<String> = varchar(ColumnData.COL_EMAIL_NAME, ColumnData.EMAIL_LEN).uniqueIndex()
    val userId = uuid(ColumnData.COL_USER_ID_NAME)
}

class EmailEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EmailEntity>(Emails)

    val emailId: UUID get() = id.value
    var email by Emails.email
    var userId by Emails.userId
}
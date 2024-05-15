package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Emails : IntIdTable() {
    val email: Column<String> = varchar(ColumnData.COL_EMAIL_NAME, ColumnData.EMAIL_LEN).uniqueIndex()
    val userId = uuid(ColumnData.COL_USER_ID_NAME)
}

class EmailEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<EmailEntity>(Emails)
    var emailId by Emails.id
    var email by Emails.email
    var userId by Emails.userId
}
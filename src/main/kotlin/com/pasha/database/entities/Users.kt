package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*


object Users : UUIDTable() {
    val username: Column<String> = varchar(ColumnData.COL_USERNAME_NAME, ColumnData.USERNAME_LEN).uniqueIndex()
    val avatarPath: Column<String?> = varchar(ColumnData.COL_AVATAR_NAME, ColumnData.SYSTEM_PATH_LEN).nullable()
    val password: Column<String> = varchar(ColumnData.COL_PASSWORD_NAME, ColumnData.PASSWORD_LEN)
    val phoneNumber: Column<String> = varchar(ColumnData.COL_PHONE_NUMBER_NAME, ColumnData.PHONE_LEN).uniqueIndex()
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(Users)

    var userId by Users.id
    var username by Users.username
    var avatarPath by Users.avatarPath
    var password by Users.password
    var phoneNumber by Users.phoneNumber
}
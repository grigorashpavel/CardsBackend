package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*


object Tokens : UUIDTable() {
    val revoked: Column<Boolean> = bool(ColumnData.COL_REVOKE_STATUS_NAME)
    val expireTime: Column<Long> = long(ColumnData.COL_EXPIRE_TIME_NAME)
    val deviceId = varchar(ColumnData.COL_DEVICE_ID_NAME, ColumnData.DEVICE_ID_LEN)
}

class TokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TokenEntity>(Tokens)

    val tokenId: UUID get() = id.value
    var isRevoked by Tokens.revoked
    var expireTime by Tokens.expireTime
    var deviceId by Tokens.deviceId
}
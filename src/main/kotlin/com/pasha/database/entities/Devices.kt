package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Devices : IntIdTable() {
    val deviceId = varchar(ColumnData.COL_DEVICE_ID_NAME, ColumnData.DEVICE_ID_LEN)
    val userId = uuid(ColumnData.COL_USER_ID_NAME)
}

class DeviceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DeviceEntity>(Devices)

    var deviceId by Devices.deviceId
    var userId by Devices.userId
}
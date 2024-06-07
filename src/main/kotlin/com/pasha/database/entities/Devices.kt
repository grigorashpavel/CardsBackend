package com.pasha.database.entities

import com.pasha.database.ColumnData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Devices : IntIdTable() {
    val deviceId = varchar(ColumnData.COL_DEVICE_ID_NAME, ColumnData.DEVICE_LEN)
    val deviceName = varchar(ColumnData.COL_DEVICE_NAME, ColumnData.DEVICE_LEN)
    val userId = uuid(ColumnData.COL_USER_ID_NAME)
}

class DeviceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DeviceEntity>(Devices)

    val entryId: Int get() = id.value
    var deviceId by Devices.deviceId
    var deviceName by Devices.deviceName
    var userId by Devices.userId
}
package com.pasha.services.database


import com.pasha.config.AppConfig
import com.pasha.database.entities.Devices
import com.pasha.database.entities.Emails
import com.pasha.database.entities.Tokens
import com.pasha.database.entities.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single


@Single
class DatabaseService(
    private val config: AppConfig
) {
    private val driver by lazy { config.appConfig.property("database.driver").getString() }
    private val url by lazy { config.appConfig.property("database.url").getString() }
    private val user by lazy { config.appConfig.property("database.user").getString() }
    private val password by lazy { config.appConfig.property("database.password").getString() }

    private val database = Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = password
    )

    fun initDatabase() = runBlocking {
        createTables()
    }

    private suspend fun createTables() = withContext(Dispatchers.IO) {
        launch {
            val tables = listOf(Devices, Emails, Tokens, Users)
            transaction(database) {
                SchemaUtils.create(tables = tables.toTypedArray())
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, database) { block() }
}
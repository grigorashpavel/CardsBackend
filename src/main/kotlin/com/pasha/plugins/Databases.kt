package com.pasha.plugins

import com.pasha.services.database.DatabaseService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.koin.ktor.ext.inject

fun Application.configureDatabases() {
    val dbService: DatabaseService by inject()
    dbService.initDatabase()
}

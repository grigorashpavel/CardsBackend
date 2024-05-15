package com.pasha

import com.pasha.config.AppConfig
import com.pasha.database.entities.UserEntity
import com.pasha.plugins.*
import com.pasha.services.database.DatabaseService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) = EngineMain.main(args)

//fun main() = runBlocking {
//    val appConf = AppConfig()
//    val database = DatabaseService(appConf)
//
//    database.dbQuery {
//        UserEntity.all().forEach { entry ->
//            println("userId ${entry.userId}")
//            println("username ${entry.username}")
//            println("password ${entry.password}")
//            println("phoneNumber ${entry.phoneNumber}")
//            println("avatarPath ${entry.avatarPath}")
//        }
//    }
//}

fun Application.module() {
    configureKoin()
    configureStatusPages()
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
package com.pasha

import com.pasha.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureLogging()
    configureKoin()
    configureStatusPages()
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
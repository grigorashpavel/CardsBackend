package com.pasha.plugins

import io.ktor.server.application.*
import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    koin {
        slf4jLogger()
        defaultModule()
    }
}
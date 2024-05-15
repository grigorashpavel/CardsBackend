package com.pasha.config

import io.ktor.server.config.*
import org.koin.core.annotation.Singleton

@Singleton
class AppConfig {
    val appConfig = ApplicationConfig("application.conf")
}
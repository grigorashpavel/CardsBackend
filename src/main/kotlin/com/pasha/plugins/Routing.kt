package com.pasha.plugins

import com.pasha.repositories.tokens.TokensRepository
import com.pasha.repositories.users.UsersRepository
import com.pasha.routes.exits.stopCurrentSession
import com.pasha.routes.exits.stopOtherSessions
import com.pasha.routes.login.loginRoute
import com.pasha.routes.register.registerRoute
import com.pasha.routes.test.testRoute
import com.pasha.routes.tokens_refresh.refreshTokensRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val usersRepository: UsersRepository by inject()
    val tokensRepository: TokensRepository by inject()

    routing {
        loginRoute(usersRepository, tokensRepository)
        registerRoute(usersRepository, tokensRepository)
        testRoute()
        refreshTokensRoute(tokensRepository)
        stopCurrentSession(tokensRepository)
        stopOtherSessions(usersRepository, tokensRepository)
    }
}

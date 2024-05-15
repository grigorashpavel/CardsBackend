package com.pasha.routes.test

import com.pasha.models.users.CredentialsDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.testRoute() {
    authenticate("auth-jwt") {
        get("/test") {
            val credentials = call.receive<CredentialsDto>()
            println(credentials.email)
            println(credentials.password)
            println(credentials.deviceId)
            //call.respond(HttpStatusCode.OK,"Test Correct Auth")
            call.respond<CredentialsDto>(credentials)
        }
    }
}
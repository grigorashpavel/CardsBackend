package com.pasha.routes.profile

import com.pasha.file_manager.FileManager
import com.pasha.plugins.Security
import com.pasha.repositories.users.UsersRepository
import com.pasha.routes.Routes
import com.pasha.util.TokenExtractor
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Route.profile(
    usersRepository: UsersRepository
) {
    authenticate(Security.AUTH_JWT.value) {
        get("${Routes.API}/${Routes.Profile}") {
            println(call.request.headers[HttpHeaders.Authorization])
            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject
            val user = usersRepository.getUser(email)
            if (user != null) {
                call.respond(
                    HttpStatusCode.OK,
                    hashMapOf(
                        "headerBackgroundPath" to "${Routes.API}/${Routes.RandomBackgrounds}",
                        "email" to user.email,
                        "username" to user.username,
                        "avatarPath" to user.avatarPath
                    )
                )
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("${Routes.API}/${Routes.AvatarProfile}") {
            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject
            if (email != null) {
                val avatar = File("./users/$email/avatar.jpg")
                if (avatar.exists()) {
                    call.respondFile(avatar)

                    return@get
                }
            }

            call.respond(HttpStatusCode.NotFound, "We can`t find your avatar! Upload it.")
        }

        post("${Routes.API}/${Routes.Profile}") {
            var username: String? = null
            var avatar: PartData.FileItem? = null

            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject
            println(token)
            println(email)
            if (email == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "username") {
                            username = part.value
                        }
                    }

                    is PartData.FileItem -> {
                        if (part.name == "avatar") {
                            avatar = part
                        }
                    }

                    else -> Unit
                }
            }

            val path = FileManager.createDirectoryIfNotExist(email)
            val isSaved = FileManager.saveFileByPartData(avatar, path)
            avatar?.dispose

            if (isSaved) {
                usersRepository.updateAvatarPath(email)
            }

            val isChanged = username?.let { usrname -> usersRepository.updateUsername(email, usrname) } == true

            when {
                isSaved.not() && isChanged.not() -> call.respond(HttpStatusCode.BadRequest)
                else -> call.respond(HttpStatusCode.OK)
            }
        }
    }
}
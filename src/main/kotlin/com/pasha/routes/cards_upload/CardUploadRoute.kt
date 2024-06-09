package com.pasha.routes.cards_upload

import com.pasha.file_manager.FileManager
import com.pasha.plugins.Security
import com.pasha.repositories.cards.CardsRepository
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


fun Route.cardUploadRoute(
    cardsRepository: CardsRepository,
    usersRepository: UsersRepository
) {
    authenticate(Security.AUTH_JWT.value) {
        post("${Routes.API}/${Routes.Upload}") {
            var cardName: String? = null
            var card: PartData.FileItem? = null

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
                        if (part.name == "card_name") {
                            cardName = part.value
                        }
                    }

                    is PartData.FileItem -> {
                        if (part.name == "card") {
                            card = part
                        }
                    }

                    else -> Unit
                }
            }

            val emailId = usersRepository.getEmailId(email)
            if (cardsRepository.cardAlreadyExist(cardName, emailId).not()) {
                val path = FileManager.createCardDirectoryIfNotExist(email)
                if (cardName != null) {
                    val isSaved = FileManager.saveFileByPartData(card, path, cardName!!)
                    card?.dispose
                    if (isSaved) {
                        cardsRepository.saveCard(
                            cardName!!,
                            path,
                            userId = usersRepository.getUserId(email),
                            emailId = usersRepository.getEmailId(email)
                        )
                    }

                    when {
                        isSaved.not() -> call.respond(HttpStatusCode.BadRequest)
                        else -> call.respond(HttpStatusCode.OK)
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Card Name can`t be empty!")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Card with name $cardName already exist!")
            }
        }
    }
}
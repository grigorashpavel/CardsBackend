package com.pasha.routes.cards

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
import java.io.File
import java.util.UUID


fun Route.cardsRoutes(
    usersRepository: UsersRepository,
    cardsRepository: CardsRepository
) {
    authenticate(Security.AUTH_JWT.value) {
        get("${Routes.API}/${Routes.AllCards}") {
            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject
            val emailId = email?.let { usersRepository.getEmailId(it) }
            if (emailId != null) {
                val cards = cardsRepository.getAllCards(emailId)
                call.respond(
                    HttpStatusCode.OK,
                    hashMapOf("cards" to cards)
                )
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("${Routes.API}/${Routes.CardById}") {
            val cardId = call.parameters["id"]?.let { UUID.fromString(it) }
            if (cardId != null) {
                val path = cardsRepository.getCardPath(cardId)
                val card = FileManager.getFile(path)
                if (card != null) {
                    call.respondFile(card)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Cards that you want see not found!"
                    )
                }
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Cards that you want see not found!"
                )
            }
        }

        get("${Routes.API}/${Routes.CardsByName}") {
            val name = call.parameters["name"]
            if (name != null) {
                val cards = cardsRepository.findSimilarCards(name)
                if (cards.isNotEmpty()) {
                    call.respond(
                        HttpStatusCode.OK,
                        hashMapOf("cards" to cards)
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Cards that you want see not found!"
                    )
                }
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Cards that you want see not found!"
                )
            }
        }

        put("${Routes.API}/${Routes.EditCard}") {
            val token = TokenExtractor.extractToken(call.request)
            val email = TokenExtractor.extractPayload(token)?.subject
            val cardId = call.parameters["id"]?.let { UUID.fromString(it) }
            if (cardId != null && email != null) {
                val multipart = call.receiveMultipart()
                var cardName = ""
                var card: PartData.FileItem? = null
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "cardName") {
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

                val path = FileManager.createCardDirectoryIfNotExist(email)
                val isSaved = FileManager.saveFileByPartData(card, path, cardName)
                if (isSaved) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error when change file. Try Again Later!")
                }
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Cards that you want edit not found!"
                )
            }
        }

        delete("${Routes.API}/${Routes.DeleteCard}") {
            val cardId = call.parameters["id"]?.let { UUID.fromString(it) }
            println(cardId)
            if (cardId != null) {
                val isDeleted = cardsRepository.deleteCard(cardId)
                println(isDeleted)
                if (isDeleted) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Cards that you want delete not found!"
                )
            }
        }
    }
}
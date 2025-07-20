package com.tiktop

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureRouting() {
    val db = mutableMapOf<String, String>()
    val jwtModel: JWTModel by inject()
    routing {
        authenticate("auth-jwt") {
            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.getClaim("userId", String::class)

                if (username != null) {
                    val password = db[username]
                    if (password != null) {
                        call.respondText("Welcome to profile, $username. Your password is $password")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found in DB")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid token or missing userId claim")
                }
            }
        }
        post("/login") {
            val request = call.receive<OAuthRequest>()
            val pwd = db[request.username]
            val isValidUser = db.containsKey(request.username)
            if (isValidUser){
                if (pwd == request.password){
//                    call.respondText("login success full")
                   val token = jwtModel.generateToken(userId = request.username)
                    call.respond(token)
                } else  call.respondText("wrong password")
            } else call.respondText("wrong username or you are not register")
        }

        post("/signup") {
            val request = call.receive<OAuthRequest>()
            val isValidUser = db.containsKey(request.username)
            if (isValidUser){
                call.respondText("already registered, login now")
            } else {
                call.respondText("registered successfully, login now")
                db[request.username] = request.password
            }
        }
    }
}

@Serializable
data class OAuthRequest(val username: String,val password: String)
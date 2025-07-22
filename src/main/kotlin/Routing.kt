package com.tiktop

import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Application.configureRouting(httpClient: HttpClient) {
    val db = mutableMapOf<String, UserInfo>()
    val jwtModel: JWTModel by inject()
    routing {
        authenticate("google_oauth") {

            get("/login") {
//                val request = call.receive<OAuthRequest>()
//                val pwd = db[request.username]
//                val isValidUser = db.containsKey(request.username)
//                if (isValidUser){
//                    if (pwd == request.password){
////                    call.respondText("login success full")
//                        val token = jwtModel.generateToken(userId = request.username)
//                        call.respond(token)
//                    } else  call.respondText("wrong password")
//                } else call.respondText("wrong username or you are not register")
            }
            get("callback"){
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                val accessToken = principal?.accessToken.toString()
                val userinfo = fetchGoogleUserInfo(httpClient,accessToken)
                userinfo?.let {
                    db[userinfo.userId] = userinfo
                    val token = jwtModel.generateToken(userId = userinfo.userId)
                    call.respond(token)
                }
            }

        }
        authenticate("auth-jwt") {
            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)

                if (userId != null) {
                    val password = db[userId]?.email
                    if (password != null) {
                        call.respondText("Welcome to profile, $userId. Your password is $password")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found in DB")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid token or missing userId claim")
                }
            }
        }


        post("/signup") {
            val request = call.receive<UserInfo>()
            val isValidUser = db.containsKey(request.userId)
            if (isValidUser){
                call.respondText("already registered, login now")
            } else {
                call.respondText("registered successfully, login now")
                db[request.userId] = request
            }
        }
    }
}

@Serializable
data class OAuthRequest(val username: String,val password: String)
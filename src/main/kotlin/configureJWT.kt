package com.tiktop

import io.ktor.client.HttpClient
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Application.configureJWT(httpClient: HttpClient) {
    val jwtModel: JWTModel by inject<JWTModel>()
    install(Authentication){
        jwt("auth-jwt") {
            realm = jwtModel.jwtRealm
            verifier(
                verifier = jwtModel.verifier()
            )
            validate { credential ->
                if (!credential.payload.getClaim("userId").asString().isNullOrBlank()) JWTPrincipal(credential.payload) else null
            }
        }
        configureGoogleOAuth(httpClient = httpClient)
    }
}


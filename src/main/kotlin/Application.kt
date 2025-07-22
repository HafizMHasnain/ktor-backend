package com.tiktop

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val httpClient = HttpClient(CIO){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
//    val jwtConfig: JWTConfig by inject<JWTConfig>()
    configureFrameworks()
    configureJWT(httpClient = httpClient)
    configureSerialization()
    configureRouting(httpClient = httpClient)
}

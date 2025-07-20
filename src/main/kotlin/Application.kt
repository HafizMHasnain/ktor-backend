package com.tiktop

import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
//    val jwtConfig: JWTConfig by inject<JWTConfig>()
    configureFrameworks()
    configureJWT()
    configureSerialization()
    configureRouting()
}

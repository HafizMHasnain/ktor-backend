package com.tiktop

// HelloModule.kt
import io.ktor.server.application.Application
import org.koin.dsl.module

val helloModule = module {
    single<HelloService> {
        HelloService {
            get<Application>().environment.log.info("Hello, World!")
        }
    }
}

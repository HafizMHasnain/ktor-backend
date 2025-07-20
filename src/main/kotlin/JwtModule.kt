package com.tiktop

// JwtModule.kt
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import org.koin.dsl.module

fun appModule(app: Application) = module {
    single {
        val jwt = app.environment.config.config("jwt")
        val secret = jwt.property("secret").getString()
        val audience = jwt.property("audience").getString()
        val issuer = jwt.property("domain").getString()
        val realm = jwt.property("realm").getString()
        val algorithm = Algorithm.HMAC256(secret)
        JWTModel(
            jwtAudience = audience,
            jwtIssuer = issuer,
            jwtRealm = realm,
            jwtSecret = secret,
            algorithm = algorithm
        )
    }
}

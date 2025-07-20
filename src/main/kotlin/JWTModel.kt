package com.tiktop

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

data class JWTModel(
    var jwtAudience: String /*= System.getenv("audience") ?: "default-audience"*/ ,
    var jwtIssuer: String /*= System.getenv("domain") ?: "https://jwt-provider-domain/"*/,
    var jwtRealm: String /*= System.getenv("realm") ?: ""*/,
    var jwtSecret: String /*= System.getenv("secret") ?: ""*/,
    var algorithm: Algorithm /* = Algorithm.HMAC256(jwtSecret)*/
)
{

    fun generateToken(userId: String): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 36_000_00 * 24))
            .sign(algorithm)
    }

    fun verifier(): JWTVerifier =
        JWT.require(algorithm)
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .build()

}
package com.tiktop

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.oauth
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import io.ktor.client.HttpClient


fun AuthenticationConfig.configureGoogleOAuth(httpClient: HttpClient){
    oauth("google_oauth") {
        urlProvider = {"http://0.0.0.0:8080/callback"}
        providerLookup = { OAuthServerSettings
            .OAuth2ServerSettings(
                name = "google",
                authorizeUrl = "http://accounts.google.com/o/oauth2/auth",
                accessTokenUrl = "http://accounts.google.com/o/oauth2/token",
                requestMethod = HttpMethod.Post,
                clientId = System.getenv("GOOGLE_CLIENT_ID"),
                clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                defaultScopes = listOf("https://www.googleapi.com/auth/userinfo.profile",
                    "https://www.googleapi.com/auth/userinfo.email"),
                extraAuthParameters = listOf("access_type" to "offline")) }
        client = httpClient
    }
}

@Serializable
data class UserInfo @OptIn(ExperimentalUuidApi::class) constructor(
    val userId: String = Uuid.random().toString(),
    val name: String,
    val email: String)
@Serializable
data class GoogleUserResponse(
    val name: String,
    val email: String)

suspend fun fetchGoogleUserInfo(httpClient: HttpClient, token: String): UserInfo? {

    val response =  httpClient.get("https://www.googleapi.com/auth/userinfo"){
        headers {
            append(HttpHeaders.Authorization, "Bearer $token")
        }
    }
    return if (response.status == HttpStatusCode.OK) {
        val googleUser = response.body<GoogleUserResponse>()
         UserInfo(name = googleUser.name, email = googleUser.name)
    } else null
}
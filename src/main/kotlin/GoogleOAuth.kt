package com.tiktop

import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.oauth

fun AuthenticationConfig.configureGoogleOAuth(){
    oauth("google_oauth") {
        urlProvider = {"http://0.0.0.0:8080/callback"}
    }
}
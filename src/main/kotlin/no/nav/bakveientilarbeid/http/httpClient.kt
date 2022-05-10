package no.nav.bakveientilarbeid.http

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.tms.token.support.tokendings.exchange.TokenXHeader
import java.net.URL

suspend inline fun <reified T> HttpClient.get(url: URL, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    request {
        url("$url")
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
    }
}
suspend inline fun <reified T> HttpClient.getWithTokenX(url: URL, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    request {
        url("$url")
        method = HttpMethod.Get
        // header(TokenXHeader.Authorization, "Bearer ${accessToken.value}")
        header("TokenXAuthorization", "Bearer ${accessToken.value}")
    }
}
suspend inline fun <reified T> HttpClient.post(url: URL): T = withContext(Dispatchers.IO) {
    request {
        url("$url")
        method = HttpMethod.Post
    }
}

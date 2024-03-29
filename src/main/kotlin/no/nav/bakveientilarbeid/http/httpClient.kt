package no.nav.bakveientilarbeid.http

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.bakveientilarbeid.auth.AccessToken
import java.net.URL

const val consumerIdHeaderName = "Nav-Consumer-Id"
const val consumerIdHeaderValue = "paw:bakveientilarbeid"

suspend inline fun <reified T> HttpClient.get(url: URL, accessToken: AccessToken): HttpResponse = withContext(Dispatchers.IO) {
    request {
        url("$url")
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
    }
}
suspend inline fun <reified T> HttpClient.getWithTokenX(url: URL, accessToken: AccessToken): HttpResponse = withContext(Dispatchers.IO) {
    request {
        url("$url")
        method = HttpMethod.Get
        header("TokenXAuthorization", "Bearer ${accessToken.value}")
    }
}

suspend inline fun <reified T> HttpClient.getWithConsumerId(url: URL, accessToken: AccessToken): HttpResponse = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
        header(consumerIdHeaderName, consumerIdHeaderValue)
    }
}

suspend inline fun <reified T> HttpClient.postWithConsumerId(url: URL, queryParams: Parameters, accessToken: AccessToken): HttpResponse = withContext(Dispatchers.IO) {
    request {
        url(url)
        url {
            queryParams.forEach { param, _ -> parameters.append(param, queryParams[param]!!) }
        }
        method = HttpMethod.Post
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
        header(consumerIdHeaderName, consumerIdHeaderValue)
    }
}

suspend inline fun <reified T> HttpClient.postWithConsumerId(url: URL, requestBody: Any, accessToken: AccessToken): HttpResponse = withContext(Dispatchers.IO) {
    request {
        url(url)
        setBody(requestBody)
        method = HttpMethod.Post
        header(HttpHeaders.ContentType, "application/json")
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
        header(consumerIdHeaderName, consumerIdHeaderValue)
    }
}

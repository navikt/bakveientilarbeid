package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.isDevelopment
import no.nav.bakveientilarbeid.config.logger
import no.nav.bakveientilarbeid.http.getWithConsumerId
import java.net.URL

fun Route.ptoProxyRoute(
    authenticatedUserService: AuthenticatedUserService,
    httpClient: HttpClient,
    PTO_PROXY_URL: String
) {

    get("/oppfolging") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val oppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/oppfolging")
        val response = httpClient.getWithConsumerId<HttpResponse>(oppfolgingUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/underoppfolging") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val underOppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")
        val response = httpClient.getWithConsumerId<HttpResponse>(underOppfolgingUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/startregistrering") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val startRegistreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/startregistrering")
        val response = httpClient.getWithConsumerId<HttpResponse>(startRegistreringUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/registrering") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val registreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
        val response = httpClient.getWithConsumerId<HttpResponse>(registreringUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/dialog/antallUleste") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val dialogUrl = URL("$PTO_PROXY_URL/veilarbdialog/api/dialog/antallUleste")
        val response = httpClient.getWithConsumerId<HttpResponse>(dialogUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/vedtakinfo/besvarelse") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val besvarelseUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/behovsvurdering/besvarelse")
        val response = httpClient.getWithConsumerId<HttpResponse>(besvarelseUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/vedtakinfo/motestotte") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val motestotteUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/motestotte")
        val response = httpClient.getWithConsumerId<HttpResponse>(motestotteUrl, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }
}


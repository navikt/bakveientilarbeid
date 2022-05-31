package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.statement.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
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
        handleRequest(call, httpClient, token, oppfolgingUrl)
    }

    get("/underoppfolging") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val underOppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")
        handleRequest(call, httpClient, token, underOppfolgingUrl)
    }

    get("/startregistrering") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val startRegistreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/startregistrering")
        handleRequest(call, httpClient, token, startRegistreringUrl)
    }

    get("/registrering") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val registreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
        handleRequest(call, httpClient, token, registreringUrl)
    }

    get("/dialog/antallUleste") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val dialogUrl = URL("$PTO_PROXY_URL/veilarbdialog/api/dialog/antallUleste")
        handleRequest(call, httpClient, token, dialogUrl)
    }

    get("/vedtakinfo/besvarelse") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val besvarelseUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/behovsvurdering/besvarelse")
        handleRequest(call, httpClient, token, besvarelseUrl)
    }

    get("/vedtakinfo/motestotte") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val motestotteUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/motestotte")
        handleRequest(call, httpClient, token, motestotteUrl)
    }
}

suspend fun handleRequest(call: ApplicationCall, httpClient: HttpClient, token: AccessToken, url: URL) {
    Result.runCatching {
        httpClient.getWithConsumerId<HttpResponse>(url, token)
    }.fold(
        onSuccess = {
            call.respondBytes(bytes = it.readBytes(), status = it.status)
        },
        onFailure = {
            val exception = it as ResponseException
            call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
        }
    )
}

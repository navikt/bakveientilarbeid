package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.statement.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.InputType
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.http.getWithConsumerId
import no.nav.bakveientilarbeid.http.postWithConsumerId
import java.net.URL

fun Route.ptoProxyRoute(
    authenticatedUserService: AuthenticatedUserService,
    httpClient: HttpClient,
    PTO_PROXY_URL: String
) {

    get("/oppfolging") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val oppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/oppfolging")
        handleRequest(call, httpClient, token, oppfolgingUrl)
    }

    get("/underoppfolging") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val underOppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")
        handleRequest(call, httpClient, token, underOppfolgingUrl)
    }

    get("/startregistrering") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val startRegistreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/startregistrering")
        handleRequest(call, httpClient, token, startRegistreringUrl)
    }

    get("/registrering") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val registreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
        handleRequest(call, httpClient, token, registreringUrl)
    }

    get("/dialog/antallUleste") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val dialogUrl = URL("$PTO_PROXY_URL/veilarbdialog/api/dialog/antallUleste")
        handleRequest(call, httpClient, token, dialogUrl)
    }

    get("/vedtakinfo/besvarelse") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val besvarelseUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/behovsvurdering/besvarelse")
        handleRequest(call, httpClient, token, besvarelseUrl)
    }

    get("/vedtakinfo/motestotte") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val motestotteUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/motestotte")
        handleRequest(call, httpClient, token, motestotteUrl)
    }

    get("/arbeidssoker/perioder") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val perioderUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/arbeidssoker/perioder")

        System.out.println("\n\n PARAMETERE ${call.request.queryParameters} \n\n")

        Result.runCatching {
            httpClient.postWithConsumerId<HttpResponse>(perioderUrl, call.request.queryParameters, token)
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

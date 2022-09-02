package no.nav.bakveientilarbeid.arbeidssoker

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.logger
import no.nav.bakveientilarbeid.http.getWithConsumerId
import java.net.URL

fun Route.arbeidssokerRoute(
    authenticatedUserService: AuthenticatedUserService,
    httpClient: HttpClient,
    PTO_PROXY_URL: String
) {
    get("/arbeidssoker") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val underOppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")

        Result.runCatching {
            httpClient.getWithConsumerId<HttpResponse>(underOppfolgingUrl, token)
        }.fold(
            onSuccess = {
                call.respondBytes(bytes = it.readBytes(), status = it.status)
            },
            onFailure = {
                val exception = it as ResponseException
                logger.warn("Feil ved kall til pto-proxy=${underOppfolgingUrl} status=${exception.response.status}")
                call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
            }
        )
    }
}

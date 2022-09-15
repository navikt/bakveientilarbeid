package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.server.application.*
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.logger
import no.nav.bakveientilarbeid.http.getWithConsumerId
import no.nav.bakveientilarbeid.http.postWithConsumerId
import org.slf4j.Logger
import java.net.URL

@Serializable
data class GjelderFraPostRequestDto(@Contextual val dato: String)

fun Route.ptoProxyRoute(
    authenticatedUserService: AuthenticatedUserService,
    httpClient: HttpClient,
    PTO_PROXY_URL: String
) {

    get("/oppfolging") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val oppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/oppfolging")
        handleRequest(call, httpClient, token, oppfolgingUrl, logger)
    }

    get("/underoppfolging") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val underOppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")
        handleRequest(call, httpClient, token, underOppfolgingUrl, logger)
    }

    get("/startregistrering") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val startRegistreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/startregistrering")
        handleRequest(call, httpClient, token, startRegistreringUrl, logger)
    }

    get("/registrering") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val registreringUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
        handleRequest(call, httpClient, token, registreringUrl, logger)
    }

    get("/standard-innsats") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val standardInnsatsUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/profilering/standard-innsats")
        handleRequest(call, httpClient, token, standardInnsatsUrl, logger)
    }

    get("/dialog/antallUleste") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val dialogUrl = URL("$PTO_PROXY_URL/veilarbdialog/api/dialog/antallUleste")
        handleRequest(call, httpClient, token, dialogUrl, logger)
    }

    get("/vedtakinfo/besvarelse") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val besvarelseUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/behovsvurdering/besvarelse")
        handleRequest(call, httpClient, token, besvarelseUrl, logger)
    }

    get("/vedtakinfo/motestotte") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val motestotteUrl = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/motestotte")
        handleRequest(call, httpClient, token, motestotteUrl, logger)
    }

    get("/arbeidssoker/perioder") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val perioderUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/arbeidssoker/perioder")
        Result.runCatching {
            httpClient.postWithConsumerId<HttpResponse>(perioderUrl, call.request.queryParameters, token)
        }.fold(
            onSuccess = {
                call.respondBytes(bytes = it.readBytes(), status = it.status)
            },
            onFailure = {
                val exception = it as ResponseException
                logger.warn("Feil ved kall til pto-proxy=${perioderUrl} status=${exception.response.status}")
                call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
            }
        )
    }

    get("/gjelderfra") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val gjelderFraUrl = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering/gjelderfra")
        handleRequest(call, httpClient, token, gjelderFraUrl, logger)
    }

    post("/gjelderfra") {
        val token = AccessToken(authenticatedUserService.getAuthenticatedUser(call).token)
        val gjelderFraUrl =  URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering/gjelderfra")

        Result.runCatching {
            val requestBody = call.receive<GjelderFraPostRequestDto>()
            httpClient.postWithConsumerId<HttpResponse>(gjelderFraUrl, requestBody, token)
        }.fold(
            onSuccess = {
                call.respondBytes(bytes = it.readBytes(), status = it.status)
            },
            onFailure = {
                when (it) {
                    is ResponseException -> {
                        logger.warn("Feil ved kall til pto-proxy=${gjelderFraUrl} status=${it.response.status}")
                        call.respondBytes(status = it.response.status, bytes = it.response.readBytes())
                    }
                    else -> {
                        logger.warn("Feil", it)
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = it.message ?: "Ukjent feil mot POST /gjelderfra"
                        )
                    }
                }
            }
        )
    }
}

suspend fun handleRequest(call: ApplicationCall, httpClient: HttpClient, token: AccessToken, url: URL, logger: Logger) {
    Result.runCatching {
        httpClient.getWithConsumerId<HttpResponse>(url, token)
    }.fold(
        onSuccess = {
            call.respondBytes(bytes = it.readBytes(), status = it.status)
        },
        onFailure = {
            val exception = it as ResponseException
            logger.warn("Feil ved kall til pto-proxy=${url} status=${exception.response.status}")
            call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
        }
    )
}

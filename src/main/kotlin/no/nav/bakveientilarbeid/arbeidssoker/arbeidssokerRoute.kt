package no.nav.bakveientilarbeid.arbeidssoker

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
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
        val perioder = hentArbeidssokerperioder(PTO_PROXY_URL, httpClient, token)
        val underoppfolging = hentUnderoppfolging(httpClient, PTO_PROXY_URL, token)

        call.respond(
            HttpStatusCode.OK,
            Arbeidssoker(
                underoppfolging = underoppfolging?.underOppfolging,
                arbeidssokerperioder = perioder
            )
        )
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.hentArbeidssokerperioder(
    PTO_PROXY_URL: String,
    httpClient: HttpClient,
    token: AccessToken
): List<Arbeidssokerperiode> {
    val fraOgMedDato = call.request.queryParameters["fraOgMed"]
    if (fraOgMedDato == null) {
        call.respond(HttpStatusCode.BadRequest, "Påkrevd query parameter fraOgMed mangler")
    }

    val tilOgMedDato = call.request.queryParameters["tilOgMed"]
    val perioderUrl = if (tilOgMedDato != null) {
        URL("$PTO_PROXY_URL/veilarbregistrering/api/arbeidssoker/perioder/niva3?fraOgMed=$fraOgMedDato&tilOgMed=$tilOgMedDato")
    } else {
        URL("$PTO_PROXY_URL/veilarbregistrering/api/arbeidssoker/perioder/niva3?fraOgMed=$fraOgMedDato")
    }

    var perioder: List<Arbeidssokerperiode> = emptyList()
    try {
        val perioderRequest = httpClient.getWithConsumerId<HttpResponse>(perioderUrl, token)
        perioder = perioderRequest.body<Arbeidssokerperioder>().arbeidssokerperioder
    } catch (e: Exception) {
        logger.error("Feil ved henting av arbeidssokerPerioder", e)
    }
    return perioder
}

private suspend fun PipelineContext<Unit, ApplicationCall>.hentUnderoppfolging(
    httpClient: HttpClient,
    PTO_PROXY_URL: String,
    token: AccessToken
): Underoppfolging? {
    val underOppfolgingUrl = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")
    var underoppfolging: Underoppfolging? = null

    try {
        val oppfolgingsRequest = httpClient.getWithConsumerId<HttpResponse>(underOppfolgingUrl, token)
        underoppfolging = oppfolgingsRequest.body<Underoppfolging>()
    } catch (e: Exception) {
        logger.error("Feil ved henting av underoppfolging", e)
    }
    return underoppfolging
}

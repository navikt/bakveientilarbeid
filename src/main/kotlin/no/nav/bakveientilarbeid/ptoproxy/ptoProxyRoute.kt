package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.response.*
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
    ptoProxyService: PtoProxyService,
    authenticatedUserService: AuthenticatedUserService,
    httpClient: HttpClient,
    PTO_PROXY_URL: String
) {

    get("/oppfolging") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentOppfolgig(authenticatedUserService.getAuthenticatedUser(call)))
    }

    get("/underoppfolging") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentUnderOppfolging(authenticatedUserService.getAuthenticatedUser(call)))
    }

    get("/startregistrering") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        if (isDevelopment()) {
            logger.info("startregistrering - token ${authenticatedUser.token}")
        }
        call.respond(HttpStatusCode.OK, ptoProxyService.hentStartRegistrering(authenticatedUser))
    }

    get("/registrering") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val token = AccessToken(authenticatedUser.token)
        val REGISTRERING_URL = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
        val response = httpClient.getWithConsumerId<HttpResponse>(REGISTRERING_URL, token)
        call.respondBytes(bytes = response.readBytes(), status = response.status)
    }

    get("/dialog/antallUleste") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentUlesteDialoger(authenticatedUserService.getAuthenticatedUser(call)))
    }

    get("/vedtakinfo/besvarelse") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentBesvarelse(authenticatedUserService.getAuthenticatedUser(call)))
    }

    get("/vedtakinfo/motestotte") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentMotestotte(authenticatedUserService.getAuthenticatedUser(call)))
    }

}

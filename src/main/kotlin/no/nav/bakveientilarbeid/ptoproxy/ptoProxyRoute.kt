package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.authenticatedUser
import no.nav.bakveientilarbeid.config.isDevelopment
import no.nav.bakveientilarbeid.config.logger
import no.nav.bakveientilarbeid.http.getWithConsumerId
import java.net.URL

fun Route.ptoProxyRoute(
    ptoProxyService: PtoProxyService,
    httpClient: HttpClient,
    PTO_PROXY_URL: String
) {

    get("/oppfolging") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentOppfolgig(authenticatedUser).toString())
    }

    get("/underoppfolging") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentUnderOppfolging(authenticatedUser).toString())
    }

    get("/startregistrering") {
        if (isDevelopment()) {
            logger.info("startregistrering - token ${authenticatedUser.token}")
        }
        call.respond(HttpStatusCode.OK, ptoProxyService.hentStartRegistrering(authenticatedUser).toString())
    }

    get("/registrering") {
        val token = AccessToken(authenticatedUser.token)
        val REGISTRERING_URL = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
        call.respond(httpClient.getWithConsumerId(REGISTRERING_URL, token))
    }

/*    get("/registrering") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentRegistrering(authenticatedUser).toString())
    }*/

    get("/dialog/antallUleste") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentUlesteDialoger(authenticatedUser).toString())
    }

    get("/vedtakinfo/besvarelse") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentBesvarelse(authenticatedUser).toString())
    }

    get("/vedtakinfo/motestotte") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentMotestotte(authenticatedUser).toString())
    }

}



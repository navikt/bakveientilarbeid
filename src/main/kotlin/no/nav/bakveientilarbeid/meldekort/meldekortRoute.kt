package no.nav.bakveientilarbeid.meldekort

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.logger

fun Route.meldekortRoute(meldekortService: MeldekortService, authenticatedUserService: AuthenticatedUserService) {

    get("/meldekort") {
        try {
            call.respond(HttpStatusCode.OK, meldekortService.hentMeldekort(authenticatedUserService.getAuthenticatedUser(call)))
        } catch (exception: Exception) {
            logger.warn("Feil ved henting av meldekort", exception)
            call.respond(HttpStatusCode.ServiceUnavailable)
        }
    }

    get("/meldekort/status") {
        try {
            call.respond(HttpStatusCode.OK, meldekortService.hentStatus(authenticatedUserService.getAuthenticatedUser(call)))
        } catch (exception: Exception) {
            logger.warn("Feil ved henting av meldekort-status", exception)
            call.respond(HttpStatusCode.ServiceUnavailable)
        }
    }
}

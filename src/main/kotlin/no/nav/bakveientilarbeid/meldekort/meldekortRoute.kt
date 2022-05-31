package no.nav.bakveientilarbeid.meldekort

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService

fun Route.meldekortRoute(meldekortService: MeldekortService, authenticatedUserService: AuthenticatedUserService) {

    get("/meldekort") {
        call.respond(HttpStatusCode.OK, meldekortService.hentMeldekort(authenticatedUserService.getAuthenticatedUser(call)))
    }

    get("/meldekort/status") {
        call.respond(HttpStatusCode.OK, meldekortService.hentStatus(authenticatedUserService.getAuthenticatedUser(call)))
    }
}

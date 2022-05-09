package no.nav.bakveientilarbeid.meldekort

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.config.authenticatedUser

fun Route.meldekortRoute(meldekortService: MeldekortService) {

    get("/meldekort/") {
        call.respond(HttpStatusCode.OK, meldekortService.hentMeldekort(authenticatedUser).toString())
    }

    get("/meldekort/status") {
        call.respond(HttpStatusCode.OK, meldekortService.hentStatus(authenticatedUser).toString())
    }
}

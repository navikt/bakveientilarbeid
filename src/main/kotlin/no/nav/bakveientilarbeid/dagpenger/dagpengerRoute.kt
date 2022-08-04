package no.nav.bakveientilarbeid.dagpenger

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService

fun Route.dagpengerRoute(dagpengerService: DagpengerService, authenticatedUserService: AuthenticatedUserService) {

    get("/dagpenger/soknad") {
        call.respond(HttpStatusCode.OK, dagpengerService.hentSoknad(authenticatedUserService.getAuthenticatedUser(call)))
    }

    get("/dagpenger/vedtak") {
        call.respond(HttpStatusCode.OK, dagpengerService.hentVedtak(authenticatedUserService.getAuthenticatedUser(call)))
    }

     get("/dagpenger/paabegynte") {
        call.respond(HttpStatusCode.OK, dagpengerService.hentPabegynteSoknader(authenticatedUserService.getAuthenticatedUser(call)))
    }
}


package no.nav.bakveientilarbeid.dagpenger

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.config.authenticatedUser

fun Route.dagpengerApi(dagpengerService: DagpengerService) {

    get("/dagpenger/soknad") {
        call.respond(HttpStatusCode.OK, dagpengerService.hentSoknad(authenticatedUser).toString())
    }
}


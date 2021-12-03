package no.nav.bakveientilarbeid.dagpenger

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.dagpengerApi(dagpengerService: DagpengerService) {

    get("/dagpenger/soknad") {
         // token = tokenDingsService.getTOkenForDagpenger()
        // dagpengerService.getSoknadForUser(token)
        call.respond(HttpStatusCode.OK, dagpengerService.hentSoknad())

    }
}


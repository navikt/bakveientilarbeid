package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.config.authenticatedUser

fun Route.ptoProxyRoute(ptoProxyService: PtoProxyService) {

    get("/oppfolging") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentOppfolgig(authenticatedUser).toString())
    }

    get("/underoppfolging") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentUnderOppfolging(authenticatedUser).toString())
    }

    get("/startregistrering") {
        val token = this.context.request.cookies["selvbetjening-idtoken"]
        call.respond(HttpStatusCode.OK, ptoProxyService.hentStartRegistrering(authenticatedUser, token!!).toString())
    }

    get("/registrering") {
        call.respond(HttpStatusCode.OK, ptoProxyService.hentRegistrering(authenticatedUser).toString())
    }

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



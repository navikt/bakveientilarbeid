package no.nav.bakveientilarbeid.hello

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.common.logging.util.logger


fun Route.helloRoute() {

    get("/is-authenticated") {
        logger.info("User is authenticated")
        call.respondText("""{ "isAuthenticated": true }""", ContentType.Application.Json)
    }
}

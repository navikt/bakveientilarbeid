package no.nav.bakveientilarbeid.hello

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.helloApi() {
    get("/is_authenticated") {
        call.respondText("""{ "isAuthenticated": true }""", ContentType.Application.Json)
    }
}

package no.nav.bakveientilarbeid.unleash

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import org.slf4j.LoggerFactory

fun Route.unleashRoute(
    authenticatedUserService: AuthenticatedUserService,
    unleashService: UnleashService
) {
    val log = LoggerFactory.getLogger(unleashService::class.java)

    get("/unleash") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val features = call.request.queryParameters.getAll("feature")

        if (features?.isNotEmpty() == true) {
            val result = unleashService.getFeatureStatus(authenticatedUser, features)
            call.respond(HttpStatusCode.OK, result)
        }

        call.respond(HttpStatusCode.BadRequest)

    }
}

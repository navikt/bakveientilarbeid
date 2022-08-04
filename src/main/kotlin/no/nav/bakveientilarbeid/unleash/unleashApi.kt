package no.nav.bakveientilarbeid.unleash

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import org.slf4j.LoggerFactory

fun Route.unleashRoute(
    authenticatedUserService: AuthenticatedUserService,
    unleashService: UnleashService
) {
    val log = LoggerFactory.getLogger(unleashService::class.java)

    get("/unleash") {
        val authenticatedUser = authenticatedUserService.getAuthenticatedUser(call)
        val queryParameters: Parameters = call.request.queryParameters
        val features = queryParameters.getAll("feature")

        try {
            if (features?.isNotEmpty() == true) {
                val result = unleashService.getFeatureStatus(authenticatedUser, features)
                call.respond(HttpStatusCode.OK, result)
            }
        } catch (exception: Exception) {
            call.respond(HttpStatusCode.ServiceUnavailable)
            log.warn("Klarte ikke å hente feature toggle. $exception", exception)
        }

        call.respond(HttpStatusCode.BadRequest)

    }
}

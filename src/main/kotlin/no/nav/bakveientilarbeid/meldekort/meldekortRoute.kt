package no.nav.bakveientilarbeid.meldekort

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.logger

fun Route.meldekortRoute(meldekortService: MeldekortService, authenticatedUserService: AuthenticatedUserService) {

    get("/meldekort") {
        Result.runCatching {
            meldekortService.hentMeldekort(authenticatedUserService.getAuthenticatedUser(call))
        }.fold(
            onSuccess = {
                call.respondBytes(bytes= it.readBytes(), status = it.status)
            },
            onFailure = {
                val exception = it as ResponseException
                logger.warn("Feil ved henting av meldekort, status=${exception.response.status}")
                call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
            }
        )
    }

    get("/meldekort/status") {
        Result.runCatching {
            meldekortService.hentStatus(authenticatedUserService.getAuthenticatedUser(call))
        }.fold(
            onSuccess = {
                call.respondBytes(bytes= it.readBytes(), status = it.status)
            },
            onFailure = {
                val exception = it as ResponseException
                logger.warn("Feil ved henting av meldekort-status, status=${exception.response.status}")
                call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
            }
        )
    }
}

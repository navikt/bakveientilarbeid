package no.nav.bakveientilarbeid.dagpenger

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.logger

fun Route.dagpengerRoute(dagpengerService: DagpengerService, authenticatedUserService: AuthenticatedUserService) {

    get("/dagpenger/soknad") {
        Result.runCatching {
            dagpengerService.hentSoknad(authenticatedUserService.getAuthenticatedUser(call))
        }.fold(
            onSuccess = {
                call.respondBytes(bytes = it.readBytes(), status = it.status)
            },
            onFailure = {
                val exception = it as ResponseException
                logger.warn("Feil ved henting av dagpenger-soknad, status=${exception.response.status}")
                call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
            }
        )
    }

    get("/dagpenger/vedtak") {
        Result.runCatching {
            dagpengerService.hentVedtak(authenticatedUserService.getAuthenticatedUser(call))
        }.fold(
            onSuccess = {
                call.respondBytes(bytes = it.readBytes(), status = it.status)
            },
            onFailure = {
                val exception = it as ResponseException
                logger.warn("Feil ved henting av dagpenger-vedtak, status=${exception.response.status}")
                call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
            }
        )
    }

     get("/dagpenger/paabegynte") {
         Result.runCatching {
             dagpengerService.hentPabegynteSoknader(authenticatedUserService.getAuthenticatedUser(call))
         }.fold(
             onSuccess = {
                 call.respondBytes(bytes = it.readBytes(), status = it.status)
             },
             onFailure = {
                 val exception = it as ResponseException
                 logger.warn("Feil ved henting av påbegynte dagpenger-soknader, status=${exception.response.status}")
                 call.respondBytes(status = exception.response.status, bytes = exception.response.readBytes())
             }
         )
    }
}

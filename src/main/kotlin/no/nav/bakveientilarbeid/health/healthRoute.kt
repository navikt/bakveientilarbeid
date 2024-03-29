package no.nav.bakveientilarbeid.health

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import no.nav.bakveientilarbeid.config.logger

fun Routing.healthRoute(healthService: HealthService) {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/internal/ping") {
        logger.info("ping")
        call.respondText(pingJsonResponse, ContentType.Application.Json)
    }

    get("/internal/isAlive") {
        logger.info("isAlive")
        call.respondText(text = "ALIVE", contentType = ContentType.Text.Plain)
    }

    get("/internal/isReady") {
        if (isReady(healthService)) {
            call.respondText(text = "READY", contentType = ContentType.Text.Plain)
        } else {
            call.respondText(text = "NOTREADY", contentType = ContentType.Text.Plain, status = HttpStatusCode.ServiceUnavailable)
        }
    }

    get("/internal/selftest") {
        call.buildSelftestPage(healthService)
    }
}

private suspend fun isReady(healthService: HealthService): Boolean {
    val healthChecks = healthService.getHealthChecks()
    return healthChecks
            .filter { healthStatus -> healthStatus.includeInReadiness }
            .all { healthStatus -> Status.OK == healthStatus.status }
}

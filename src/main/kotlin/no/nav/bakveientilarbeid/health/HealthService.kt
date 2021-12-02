package no.nav.bakveientilarbeid.health

import no.nav.bakveientilarbeid.config.ApplicationContext

class HealthService(private val applicationContext: ApplicationContext) {

    suspend fun getHealthChecks(): List<HealthStatus> {
        return emptyList()
    }
}

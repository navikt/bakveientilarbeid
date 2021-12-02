package no.nav.bakveientilarbeid.health

interface HealthCheck {

    suspend fun status(): HealthStatus

}

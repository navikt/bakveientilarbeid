package no.nav.bakveientilarbeid.config

import no.nav.bakveientilarbeid.health.HealthService

class ApplicationContext {

    val httpClient = HttpClientBuilder.build()
    val healthService = HealthService(this)

}

package no.nav.bakveientilarbeid.config

import no.nav.bakveientilarbeid.dagpenger.DagpengerConsumer
import no.nav.bakveientilarbeid.dagpenger.DagpengerService
import no.nav.bakveientilarbeid.health.HealthService

class ApplicationContext {

    val httpClient = HttpClientBuilder.build()

    val dagpengerConsumer = DagpengerConsumer(httpClient)

    val healthService = HealthService(this)
    val dagpengerService = DagpengerService(dagpengerConsumer)

}

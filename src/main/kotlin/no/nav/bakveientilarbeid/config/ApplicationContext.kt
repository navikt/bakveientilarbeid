package no.nav.bakveientilarbeid.config

import no.nav.bakveientilarbeid.dagpenger.DagpengerConsumer
import no.nav.bakveientilarbeid.dagpenger.DagpengerService
import no.nav.bakveientilarbeid.dagpenger.DagpengerTokendings
import no.nav.bakveientilarbeid.health.HealthService
import no.nav.bakveientilarbeid.http.HttpClientBuilder
import no.nav.bakveientilarbeid.meldekort.MeldekortConsumer
import no.nav.bakveientilarbeid.meldekort.MeldekortService
import no.nav.bakveientilarbeid.meldekort.MeldekortTokendings
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

class ApplicationContext {

    val httpClient = HttpClientBuilder.build()

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService(maxCachedEntries = 5000)
    val dagpengerTokendings = DagpengerTokendings(tokendingsService)
    val meldekortTokendings = MeldekortTokendings(tokendingsService)

    val dagpengerConsumer = DagpengerConsumer(httpClient)
    val meldekortConsumer = MeldekortConsumer(httpClient)

    val healthService = HealthService(this)
    val dagpengerService = DagpengerService(dagpengerConsumer, dagpengerTokendings)
    val meldekortService = MeldekortService(meldekortConsumer, meldekortTokendings)
}

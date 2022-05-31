package no.nav.bakveientilarbeid.config

import io.mockk.mockk
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.dagpenger.DagpengerConsumer
import no.nav.bakveientilarbeid.dagpenger.DagpengerService
import no.nav.bakveientilarbeid.dagpenger.DagpengerTokendings
import no.nav.bakveientilarbeid.health.HealthService
import no.nav.bakveientilarbeid.http.HttpClientBuilder
import no.nav.bakveientilarbeid.meldekort.MeldekortConsumer
import no.nav.bakveientilarbeid.meldekort.MeldekortService
import no.nav.bakveientilarbeid.meldekort.MeldekortTokendings
import no.nav.bakveientilarbeid.ptoproxy.PtoProxyConsumer
import no.nav.bakveientilarbeid.ptoproxy.PtoProxyService
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class ApplicationContextLocal {
    val environment = Environment()
    val httpClient = HttpClientBuilder.build()
    val authenticatedUserService = mockk<AuthenticatedUserService>(relaxed = true)

    val tokendingsService = mockk<TokendingsService>()
    val dagpengerTokendings = DagpengerTokendings(tokendingsService)
    val meldekortTokendings = MeldekortTokendings(tokendingsService)

    val dagpengerConsumer = DagpengerConsumer(httpClient)
    val meldekortConsumer = MeldekortConsumer(httpClient)
    val ptoProxyConsumer = PtoProxyConsumer(httpClient, environment.ptoProxyUrl)

    val healthService = mockk<HealthService>()
    val dagpengerService = DagpengerService(dagpengerConsumer, dagpengerTokendings)
    val meldekortService = MeldekortService(meldekortConsumer, meldekortTokendings)
    val ptoProxyService = PtoProxyService(ptoProxyConsumer);
}

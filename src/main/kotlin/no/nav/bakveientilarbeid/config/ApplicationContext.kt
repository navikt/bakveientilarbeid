package no.nav.bakveientilarbeid.config

import no.finn.unleash.DefaultUnleash
import no.finn.unleash.Unleash
import no.finn.unleash.util.UnleashConfig
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.dagpenger.DagpengerConsumer
import no.nav.bakveientilarbeid.dagpenger.DagpengerService
import no.nav.bakveientilarbeid.dagpenger.DagpengerTokendings
import no.nav.bakveientilarbeid.database.PostgreSqlDatabase
import no.nav.bakveientilarbeid.database.ProfilRepositoryImpl
import no.nav.bakveientilarbeid.health.HealthService
import no.nav.bakveientilarbeid.http.HttpClientBuilder
import no.nav.bakveientilarbeid.meldekort.MeldekortConsumer
import no.nav.bakveientilarbeid.meldekort.MeldekortService
import no.nav.bakveientilarbeid.meldekort.MeldekortTokendings
import no.nav.bakveientilarbeid.profil.ProfilService
import no.nav.bakveientilarbeid.unleash.ByEnvironmentStrategy
import no.nav.bakveientilarbeid.unleash.UnleashService
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

class ApplicationContext {
    val httpClient = HttpClientBuilder.build()
    val environment = Environment()

    val authenticatedUserService = AuthenticatedUserService()
    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService(maxCachedEntries = 5000)
    val dagpengerTokendings = DagpengerTokendings(tokendingsService)
    val meldekortTokendings = MeldekortTokendings(tokendingsService)

    val dagpengerConsumer = DagpengerConsumer(httpClient)
    val meldekortConsumer = MeldekortConsumer(httpClient)

    val healthService = HealthService(this)
    val dagpengerService = DagpengerService(dagpengerConsumer, dagpengerTokendings)
    val meldekortService = MeldekortService(meldekortConsumer, meldekortTokendings)

    val unleashClient = createUnleashClient(environment)
    val unleashService = UnleashService(unleashClient)

    private fun createUnleashClient(environment: Environment): Unleash {
        val unleashUrl = environment.unleashApiUrl

        val appName = "bakveientilarbeid"
        val envContext = if (isDevelopment()) "dev" else "prod"

        val byEnvironment = ByEnvironmentStrategy(envContext)

        val config = UnleashConfig.builder()
            .appName(appName)
            .unleashAPI(unleashUrl)
            .build()

        return DefaultUnleash(config, byEnvironment)
    }

    val postgreSqlDatabase = PostgreSqlDatabase(mapOf(
        "dbUrl" to environment.dbUrl,
        "dbUser" to environment.dbUser,
        "dbPassword" to environment.dbPassword
    ))

    val profilRepositoryImpl = ProfilRepositoryImpl(postgreSqlDatabase)
    val profilService = ProfilService(profilRepositoryImpl)
}

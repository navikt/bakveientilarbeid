package no.nav.bakveientilarbeid.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import no.nav.bakveientilarbeid.dagpenger.dagpengerRoute
import no.nav.bakveientilarbeid.health.healthRoute
import no.nav.bakveientilarbeid.http.jsonConfig
import no.nav.bakveientilarbeid.meldekort.meldekortRoute
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.personbruker.dittnav.common.security.AuthenticatedUserFactory
import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {
    val environment = Environment()
    val config = this.environment.config

    install(DefaultHeaders)

    install(CORS) {
        host(environment.corsAllowedOrigins)
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    install(Authentication) {
        tokenValidationSupport(config = config)
    }

    routing {
        healthRoute(appContext.healthService)

        authenticate {
            dagpengerRoute(appContext.dagpengerService)
            meldekortRoute(appContext.meldekortService)
        }
    }

    configureShutdownHook(appContext.httpClient)
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

val PipelineContext<Unit, ApplicationCall>.authenticatedUser: AuthenticatedUser
    get() = AuthenticatedUserFactory.createNewAuthenticatedUser(call)

package no.nav.bakveientilarbeid.config

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import no.nav.bakveientilarbeid.dagpenger.dagpengerRoute
import no.nav.bakveientilarbeid.health.healthRoute
import no.nav.bakveientilarbeid.http.jsonConfig
import no.nav.bakveientilarbeid.meldekort.meldekortRoute
import no.nav.bakveientilarbeid.ptoproxy.ptoProxyRoute
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.personbruker.dittnav.common.security.AuthenticatedUserFactory
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.localModule() {
    val appContext = ApplicationContextLocal()
    val environment = Environment()

    install(DefaultHeaders)

    install(CallId) {
        retrieve { call ->
            listOf(
                HttpHeaders.XCorrelationId,
                "Nav-Call-Id",
                "Nav-CallId"
            ).firstNotNullOfOrNull { call.request.header(it) }
        }

        generate {
            UUID.randomUUID().toString()
        }

        verify { callId: String ->
            callId.isNotEmpty()
        }
    }

    install(CallLogging) {
        callIdMdc("callId")

        mdc("requestId") { call -> call.request.header(HttpHeaders.XRequestId) ?: UUID.randomUUID().toString() }
    }

    install(CORS) {
        host(environment.corsAllowedOrigins, schemes = listOf(environment.corsAllowedSchemes))
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    routing {
        healthRoute(appContext.healthService)
        dagpengerRoute(appContext.dagpengerService, appContext.authenticatedUserService)
        meldekortRoute(appContext.meldekortService, appContext.authenticatedUserService)
        ptoProxyRoute(
            appContext.authenticatedUserService,
            appContext.httpClient,
            environment.ptoProxyUrl
        )
    }

    configureShutdownHook(appContext.httpClient)
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

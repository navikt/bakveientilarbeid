package no.nav.bakveientilarbeid.config

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import no.nav.bakveientilarbeid.arbeidssoker.arbeidssokerRoute
import no.nav.bakveientilarbeid.dagpenger.dagpengerRoute
import no.nav.bakveientilarbeid.health.healthRoute
import no.nav.bakveientilarbeid.meldekort.meldekortRoute
import no.nav.bakveientilarbeid.profil.profilRoute
import no.nav.bakveientilarbeid.ptoproxy.ptoProxyRoute
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.localModule(
    appContext: ApplicationContextLocal = ApplicationContextLocal()
) {
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
        allowHost(environment.corsAllowedOrigins, schemes = listOf(environment.corsAllowedSchemes))
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
    }

    install(ContentNegotiation) {
        json(Json {
            this.encodeDefaults = false
        })
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
        profilRoute(appContext.authenticatedUserService, appContext.profilService)
        arbeidssokerRoute(
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

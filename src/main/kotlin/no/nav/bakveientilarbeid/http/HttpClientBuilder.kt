package no.nav.bakveientilarbeid.http

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

object HttpClientBuilder {

    fun build(): HttpClient {
        return HttpClient(Apache) {
            install(ContentNegotiation) {
                json(jsonConfig())
            }
            install(HttpTimeout)
        }
    }

}

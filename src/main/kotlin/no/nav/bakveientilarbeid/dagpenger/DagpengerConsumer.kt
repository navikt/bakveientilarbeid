package no.nav.bakveientilarbeid.dagpenger

import io.ktor.client.*
import kotlinx.serialization.json.Json
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.get
import java.net.URL

class DagpengerConsumer(private val httpClient: HttpClient) {
    suspend fun hentSoknad(userToken: AccessToken): String {
        return httpClient.get(SOKNAD_URL, userToken)
    }

    companion object {
        private const val DAGPENGER_INNSYN_URL = "http://dp-innsyn.teamdagpenger.svc.cluster.local"
        private val SOKNAD_URL = URL("$DAGPENGER_INNSYN_URL/soknad")
    }
}
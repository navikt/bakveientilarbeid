package no.nav.bakveientilarbeid.dagpenger

import io.ktor.client.*
import io.ktor.client.statement.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.http.get
import java.net.URL

class DagpengerConsumer(private val httpClient: HttpClient) {
    suspend fun hentSoknad(userToken: AccessToken): HttpResponse {
        return httpClient.get<String>(SOKNAD_URL, userToken)
    }

    suspend fun hentVedtak(userToken: AccessToken): HttpResponse {
        return httpClient.get<String>(VEDTAK_URL, userToken)
    }

    suspend fun hentPabegynteSoknader(userToken: AccessToken): HttpResponse {
        return httpClient.get<String>(PABEGYNTE_SOKNADER_URL, userToken)
    }

    companion object {
        private const val DAGPENGER_INNSYN_URL = "http://dp-innsyn.teamdagpenger.svc.cluster.local"
        private val SOKNAD_URL = URL("$DAGPENGER_INNSYN_URL/soknad")
        private val VEDTAK_URL = URL("$DAGPENGER_INNSYN_URL/vedtak")
        private val PABEGYNTE_SOKNADER_URL = URL("$DAGPENGER_INNSYN_URL/paabegynte")
    }
}

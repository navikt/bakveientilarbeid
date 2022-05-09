package no.nav.bakveientilarbeid.meldekort

import io.ktor.client.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.requireMeldekortAppName
import no.nav.bakveientilarbeid.http.get
import java.net.URL

class MeldekortConsumer(private val httpClient: HttpClient) {
    suspend fun hentMeldekort(userToken: AccessToken): String {
        return httpClient.get(NESTE_MELDEKORT_URL, userToken )
    }

    suspend fun hentStatus(userToken: AccessToken): String {
        return httpClient.get(MELDEKORTSTATUS_URL, userToken)
    }

    companion object {
        private val MELDEKORT_URL = "http://${requireMeldekortAppName()}.meldekort.svc.cluster.local"
        private val NESTE_MELDEKORT_URL =  URL("$MELDEKORT_URL/api/person/meldekort")
        private val MELDEKORTSTATUS_URL = URL("$MELDEKORT_URL/api/person/meldekortstatus")
    }
}

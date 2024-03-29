package no.nav.bakveientilarbeid.meldekort

import io.ktor.client.*
import io.ktor.client.statement.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.requireMeldekortAppName
import no.nav.bakveientilarbeid.http.get
import no.nav.bakveientilarbeid.http.getWithTokenX
import java.net.URL

class MeldekortConsumer(private val httpClient: HttpClient) {
    suspend fun hentMeldekort(userToken: AccessToken): HttpResponse {
        return httpClient.getWithTokenX<String>(NESTE_MELDEKORT_URL, userToken)
    }

    suspend fun hentStatus(userToken: AccessToken): HttpResponse {
        return httpClient.getWithTokenX<String>(MELDEKORTSTATUS_URL, userToken)
    }

    companion object {
        private val MELDEKORT_URL = "http://${requireMeldekortAppName()}.meldekort.svc.cluster.local/meldekort/meldekort-api/api/person"
        private val NESTE_MELDEKORT_URL =  URL("$MELDEKORT_URL/meldekort")
        private val MELDEKORTSTATUS_URL = URL("$MELDEKORT_URL/meldekortstatus")
    }
}

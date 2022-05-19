package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.client.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.http.getWithConsumerId
import java.net.URL

class PtoProxyConsumer(
    private val httpClient: HttpClient,
    private val PTO_PROXY_URL: String
) {

    private val OPPFOLGING_URL = URL("$PTO_PROXY_URL/veilarboppfolging/api/oppfolging")
    private val UNDER_OPPFOLGING_URL = URL("$PTO_PROXY_URL/veilarboppfolging/api/niva3/underoppfolging")
    private val START_REGISTRERING_URL = URL("$PTO_PROXY_URL/veilarbregistrering/api/startregistrering")
    private val REGISTRERING_URL = URL("$PTO_PROXY_URL/veilarbregistrering/api/registrering")
    private val DIALOG_URL = URL("$PTO_PROXY_URL/veilarbdialog/api/dialog/antallUleste")
    private val BESVARELSE_URL = URL("$PTO_PROXY_URL/veilarbvedtakinfo/api/behovsvurdering/besvarelse")
    private val MOTESTOTTE_URL = URL("$PTO_PROXY_URL/veilaveilarbvedtakinfo/api/motestotte")

    suspend fun hentOppfolging(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(OPPFOLGING_URL, userToken)
    }

    suspend fun hentUnderOppfolging(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(UNDER_OPPFOLGING_URL, userToken)
    }

    suspend fun hentStartRegistrering(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(START_REGISTRERING_URL, userToken)
    }

    suspend fun hentRegistrering(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(REGISTRERING_URL, userToken)
    }

    suspend fun hentUlesteDialoger(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(DIALOG_URL, userToken)
    }

    suspend fun hentBesvarelse(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(BESVARELSE_URL, userToken)
    }

    suspend fun hentMotestotte(userToken: AccessToken): String {
        return httpClient.getWithConsumerId(MOTESTOTTE_URL, userToken)
    }

}

package no.nav.bakveientilarbeid.dagpenger


import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.requireClusterName
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class DagpengerTokendings(
    private val tokendingsService: TokendingsService,
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, DP_INNSYN_CLIENT_ID))
    }
    companion object {
        private val DP_INNSYN_CLIENT_ID = "${requireClusterName()}:teamdagpenger:dp-innsyn"
    }
}

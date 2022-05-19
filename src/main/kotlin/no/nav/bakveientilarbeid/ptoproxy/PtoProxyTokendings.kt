package no.nav.bakveientilarbeid.ptoproxy


import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.requireClusterName
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class PtoProxyTokendings(
    private val tokendingsService: TokendingsService,
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, PTO_PROXY_CLIENT_ID))
    }
    companion object {
        private val PTO_PROXY_CLIENT_ID = "${requireClusterName()}:pto:pto-proxy"
    }
}

package no.nav.bakveientilarbeid.meldekort

import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.bakveientilarbeid.config.requireClusterName
import no.nav.bakveientilarbeid.config.requireMeldekortAppName
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class MeldekortTokendings(
    private val tokendingsService: TokendingsService
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, MELDEKORT_CLIENT_ID))
    }
    companion object {
        private val MELDEKORT_CLIENT_ID = "${requireClusterName()}:meldekort:${requireMeldekortAppName()}"
    }
}

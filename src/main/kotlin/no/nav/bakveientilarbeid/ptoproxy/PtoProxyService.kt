package no.nav.bakveientilarbeid.ptoproxy

import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class PtoProxyService(private val ptoProxyConsumer: PtoProxyConsumer, private val ptoProxyTokendings: PtoProxyTokendings){

    suspend fun hentToken(user: AuthenticatedUser) = ptoProxyTokendings.exchangeToken(user)

    suspend fun hentOppfolgig(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentOppfolging(token)
    }

    suspend fun hentUnderOppfolging(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentUnderOppfolging(token)
    }

    suspend fun hentStartRegistrering(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentStartRegistrering(token)
    }

    suspend fun hentRegistrering(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentRegistrering(token)
    }

    suspend fun hentUlesteDialoger(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentUlesteDialoger(token)
    }

    suspend fun hentBesvarelse(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentBesvarelse(token)
    }

    suspend fun hentMotestotte(user: AuthenticatedUser): String {
        val token = hentToken(user)

        return ptoProxyConsumer.hentMotestotte(token)
    }

}

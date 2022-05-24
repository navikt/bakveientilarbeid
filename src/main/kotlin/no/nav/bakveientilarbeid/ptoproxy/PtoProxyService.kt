package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.request.*
import no.nav.bakveientilarbeid.auth.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class PtoProxyService(private val ptoProxyConsumer: PtoProxyConsumer) {


    suspend fun hentOppfolgig(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentOppfolging(token)
    }

    suspend fun hentUnderOppfolging(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentUnderOppfolging(token)
    }

    suspend fun hentStartRegistrering(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentStartRegistrering(token)
    }

    suspend fun hentRegistrering(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentRegistrering(token)
    }

    suspend fun hentUlesteDialoger(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentUlesteDialoger(token)
    }

    suspend fun hentBesvarelse(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentBesvarelse(token)
    }

    suspend fun hentMotestotte(user: AuthenticatedUser): String {
        val token = AccessToken(user.token)

        return ptoProxyConsumer.hentMotestotte(token)
    }

}

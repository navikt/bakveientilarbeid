package no.nav.bakveientilarbeid.meldekort

import io.ktor.client.statement.*
import no.nav.bakveientilarbeid.config.logger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class MeldekortService(
    private val meldekortConsumer: MeldekortConsumer,
    private val meldekortTokendings: MeldekortTokendings
) {

    suspend fun hentToken(user: AuthenticatedUser) = meldekortTokendings.exchangeToken(user)

    suspend fun hentMeldekort(user: AuthenticatedUser): HttpResponse {
        logger.info("Henter siste meldekort for bruker")
        val token = hentToken(user)
        return meldekortConsumer.hentMeldekort(token)
    }

    suspend fun hentStatus(user: AuthenticatedUser): HttpResponse {
        logger.info("Henter siste meldekort-status for bruker")
        val token = hentToken(user)
        return meldekortConsumer.hentStatus(token)
    }
}

package no.nav.bakveientilarbeid.dagpenger

import io.ktor.client.statement.*
import no.nav.bakveientilarbeid.config.logger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class DagpengerService(
    private val dagpengerConsumer: DagpengerConsumer,
    private val dagpengerTokendings: DagpengerTokendings
) {

    suspend fun hentToken(user: AuthenticatedUser) = dagpengerTokendings.exchangeToken(user)

    suspend fun hentSoknad(user: AuthenticatedUser): HttpResponse {
        logger.info("Henter dagpengesøknader for bruker")
        val token = hentToken(user)
        return dagpengerConsumer.hentSoknad(token)
    }

    suspend fun hentVedtak(user: AuthenticatedUser): HttpResponse {
        logger.info("Henter dagpengevedtak for bruker")
        val token = hentToken(user)
        return dagpengerConsumer.hentVedtak(token)
    }

    suspend fun hentPabegynteSoknader(user: AuthenticatedUser): HttpResponse {
        logger.info("Henter påbegynte dagpengesøknader for bruker")
        val token = hentToken(user)
        return dagpengerConsumer.hentPabegynteSoknader(token)
    }
}

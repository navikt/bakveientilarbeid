package no.nav.bakveientilarbeid.dagpenger

import io.ktor.client.statement.*
import no.nav.bakveientilarbeid.config.logger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class DagpengerService(
    private val dagpengerConsumer: DagpengerConsumer,
    private val dagpengerTokendings: DagpengerTokendings
) {

    suspend fun hentToken(user: AuthenticatedUser) = dagpengerTokendings.exchangeToken(user)

    suspend fun hentSoknad(user: AuthenticatedUser): String {
        logger.info("Henter dagpengesøknader for bruker")
        val token = hentToken(user)
        return dagpengerConsumer.hentSoknad(token).bodyAsText()
    }

    suspend fun hentVedtak(user: AuthenticatedUser): String {
        logger.info("Henter dagpengevedtak for bruker")
        val token = hentToken(user)
        return dagpengerConsumer.hentVedtak(token).bodyAsText()
    }

    suspend fun hentPabegynteSoknader(user: AuthenticatedUser): String {
        logger.info("Henter påbegynte dagpengesøknader for bruker")
        val token = hentToken(user)
        return dagpengerConsumer.hentPabegynteSoknader(token).bodyAsText()
    }
}

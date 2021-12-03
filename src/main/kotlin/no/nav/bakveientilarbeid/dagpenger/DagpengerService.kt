package no.nav.bakveientilarbeid.dagpenger

import kotlinx.serialization.json.Json
import no.nav.personbruker.dittnav.common.logging.util.logger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class DagpengerService(
    private val dagpengerConsumer: DagpengerConsumer,
    private val dagpengerTokendings: DagpengerTokendings
) {
    suspend fun hentSoknad(user: AuthenticatedUser): Json {
        logger.info("Henter dagpengesøknader for bruker")
        val token = dagpengerTokendings.exchangeToken(user)
        return dagpengerConsumer.hentSoknad(token)
    }
}
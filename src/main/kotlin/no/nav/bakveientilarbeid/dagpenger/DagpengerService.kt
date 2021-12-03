package no.nav.bakveientilarbeid.dagpenger

import kotlinx.serialization.json.Json
import no.nav.personbruker.dittnav.common.logging.util.logger

class DagpengerService(private val dagpengerConsumer: DagpengerConsumer) {
    suspend fun hentSoknad(): Json {
        logger.info("Henter dagpengesøknader for bruker")
        return dagpengerConsumer.hentSoknad()
    }
}
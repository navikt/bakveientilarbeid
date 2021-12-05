package no.nav.bakveientilarbeid.dagpenger

import com.nimbusds.jwt.JWTParser.parse
import kotlinx.serialization.json.Json
import no.nav.bakveientilarbeid.config.ifDevelopment
import no.nav.personbruker.dittnav.common.logging.util.logger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class DagpengerService(
    private val dagpengerConsumer: DagpengerConsumer,
    private val dagpengerTokendings: DagpengerTokendings
) {
    suspend fun hentSoknad(user: AuthenticatedUser): Json {
        logger.info("Henter tokenx for bruker")
        val token = dagpengerTokendings.exchangeToken(user)
        logger.info("Fant tokenx for bruker $token")
        val jwt = parse(token.value)
        ifDevelopment {
            logger.info(
                jwt.header.toString() +
                jwt.jwtClaimsSet.toString()
            ) }
        logger.info("Henter dagpengesøknader for bruker")
        return dagpengerConsumer.hentSoknad(token)
    }
}
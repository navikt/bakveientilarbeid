package no.nav.bakveientilarbeid.unleash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.finn.unleash.Unleash
import no.finn.unleash.UnleashContext
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class UnleashService(private val unleashClient: Unleash) {

    suspend fun getFeatureStatus(user: AuthenticatedUser, features: List<String>): Map<String, Boolean> = withContext(Dispatchers.IO) {
        transformToMap(user, features)
    }

    private fun transformToMap(user: AuthenticatedUser, features: List<String>): Map<String, Boolean> {
        return features.associateWith { unleashClient.isEnabled(it, createUnleashContext(user), false) }
    }

    private fun createUnleashContext(user: AuthenticatedUser): UnleashContext {
        return UnleashContext.builder()
            .userId(user.ident)
            .build()
    }

}

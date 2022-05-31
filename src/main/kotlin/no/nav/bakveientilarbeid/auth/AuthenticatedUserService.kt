package no.nav.bakveientilarbeid.auth

import io.ktor.application.*
import no.nav.personbruker.dittnav.common.security.AuthenticatedUserFactory

class AuthenticatedUserService {
    fun getAuthenticatedUser(call: ApplicationCall) = AuthenticatedUserFactory.createNewAuthenticatedUser(call)
}

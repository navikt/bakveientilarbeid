package no.nav.bakveientilarbeid.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.personbruker.dittnav.common.security.IdentityClaim
import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.v2.TokenValidationContextPrincipal
import java.time.Instant

class AuthenticatedUserService {
    private val IDENT_CLAIM: IdentityClaim
    private val defaultClaim = IdentityClaim.SUBJECT
    private val oidcIdentityClaimName = "OIDC_CLAIM_CONTAINING_THE_IDENTITY"

    private val NAV_ESSO_COOKIE_NAME = "nav-esso"

    init {
        val identityClaimFromEnvVariable = System.getenv(oidcIdentityClaimName) ?: defaultClaim.claimName
        IDENT_CLAIM = IdentityClaim.fromClaimName(identityClaimFromEnvVariable)
    }

    fun getAuthenticatedUser(call: ApplicationCall): AuthenticatedUser {
        val principal = call.principal<TokenValidationContextPrincipal>()
            ?: throw Exception("Principal har ikke blitt satt for authentication context.")

        val essoToken = getEssoTokenIfPresent(call)

        return createNewAuthenticatedUser(principal, essoToken)
    }

    private fun createNewAuthenticatedUser(principal: TokenValidationContextPrincipal, essoToken: String? = null): AuthenticatedUser {
        val token = principal.context.firstValidToken?.get()
            ?: throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje.")

        val ident: String = token.jwtTokenClaims.getStringClaim(IDENT_CLAIM.claimName)
        val loginLevel =
            extractLoginLevel(
                token
            )
        val expirationTime =
            getTokenExpirationLocalDateTime(
                token
            )

        return AuthenticatedUser(ident, loginLevel, token.tokenAsString, expirationTime, essoToken)
    }

    private fun getEssoTokenIfPresent(call: ApplicationCall): String? {
        return call.request.cookies[NAV_ESSO_COOKIE_NAME]
            ?: call.request.headers[NAV_ESSO_COOKIE_NAME]
    }

    private fun extractLoginLevel(token: JwtToken): Int {

        return when (token.jwtTokenClaims.getStringClaim("acr")) {
            "Level3" -> 3
            "Level4" -> 4
            else -> throw Exception("Innloggingsnivå ble ikke funnet. Dette skal ikke kunne skje.")
        }
    }

    private fun getTokenExpirationLocalDateTime(token: JwtToken): Instant {
        return token.jwtTokenClaims
            .expirationTime
            .toInstant()
    }
}

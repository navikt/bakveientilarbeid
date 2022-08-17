package no.nav.bakveientilarbeid.profil

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService

fun Route.profilRoute(authenticatedUserService: AuthenticatedUserService, profilService: ProfilService) {
    get("/profil") {
        try {
            val user = authenticatedUserService.getAuthenticatedUser(call)
            val profil = profilService.hentProfil(user)

            if (profil == null) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, ProfilDto.fra(profil))
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

}

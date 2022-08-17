package no.nav.bakveientilarbeid.profil

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService

fun Route.profilRoute(authenticatedUserService: AuthenticatedUserService, profilService: ProfilService) {
    get("/profil") {
        val user = authenticatedUserService.getAuthenticatedUser(call)

        try {
            val profilEntity = profilService.hentProfil(user)

            if (profilEntity == null) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, profilEntity.profil)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post("/profil") {
        val user = authenticatedUserService.getAuthenticatedUser(call)

        try {
            val profil = call.receive<ProfilJson>()
            profilService.lagreProfil(user, profil)

            call.respond(HttpStatusCode.Created, profil)
        } catch (e: SerializationException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

}

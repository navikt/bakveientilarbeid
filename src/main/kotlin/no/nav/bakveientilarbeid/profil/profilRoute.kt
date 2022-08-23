package no.nav.bakveientilarbeid.profil

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import no.nav.bakveientilarbeid.auth.AuthenticatedUserService
import no.nav.bakveientilarbeid.config.logger

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
            logger.warn("Feil ved henting av bruker profil", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    post("/profil") {
        val user = authenticatedUserService.getAuthenticatedUser(call)

        try {
            val requestBody = call.receiveText();
            val profil = Json.decodeFromString<ProfilJson>(requestBody)

            profilService.lagreProfil(user, profil)

            call.respond(HttpStatusCode.Created, profil)
        } catch (e: SerializationException) {
            logger.warn("Feil med serialisering av profil-payload", e)
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            logger.warn("Feil ved opprettelse av profil", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

}

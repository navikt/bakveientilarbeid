package no.nav.bakveientilarbeid.ptoproxy

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test


internal class ptoProxyRouteTest {

    @Test
    fun `GET registrering returnerer 204 når den får det fra veilarbregistrering`() = withTestApplication() {
        val status = handleRequest(HttpMethod.Get, "/registrering")
            .response.status()
    }

//    private fun Application.testApi() = withEnvironment(envVars) {
//
//        routing {
//            get("/test") {
//                call.respond(HttpStatusCode.OK)
//            }
//        }
//    }
}

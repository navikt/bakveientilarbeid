package no.nav.bakveientilarbeid.ptoproxy

import com.github.tomakehurst.wiremock.WireMockServer
import io.ktor.http.*
import io.ktor.server.testing.*
import no.nav.bakveientilarbeid.testsupport.TestApplicationExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestApplicationExtension::class)
internal class PtoProxyRouteTest(
    private val testApplicationEngine: TestApplicationEngine,
    private val wireMockServer: WireMockServer
) {

    @BeforeEach
    fun setup() {
        wireMockServer.start()
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `GET registrering returnerer 204 når den får det fra veilarbregistrering`() {
        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/registrering") {}.apply {
                assertEquals(HttpStatusCode.NoContent, this.response.status())
            }
        }
    }

    @Test
    fun `GET startregistrering returnerer 200 og body når den får det fra veilarbregistrering`() {
        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/startregistrering") {}.apply {
                assertEquals(HttpStatusCode.OK, this.response.status())
                assertEquals("{\"startregistrering\":\"test\"}", this.response.content)
            }
        }
    }
}

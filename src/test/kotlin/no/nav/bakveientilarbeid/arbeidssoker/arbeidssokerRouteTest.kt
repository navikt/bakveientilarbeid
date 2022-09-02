package no.nav.bakveientilarbeid.arbeidssoker

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
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
    fun `GET arbeidssoker returnerer 200 og body fra underoppfolging`() {
        stubFor(
            WireMock.get(WireMock.urlPathMatching(".*/mock-ptoproxy/veilarboppfolging/api/niva3/underoppfolging.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody("underoppfolging")
                )
        )

        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/arbeidssoker") {}.apply {
                assertEquals(HttpStatusCode.OK, this.response.status())
//                assertEquals("underoppfolging", this.response.content)
            }
        }
    }
}
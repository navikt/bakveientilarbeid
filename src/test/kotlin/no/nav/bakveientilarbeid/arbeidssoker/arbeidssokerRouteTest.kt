package no.nav.bakveientilarbeid.arbeidssoker

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.okJson
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
                    okJson("""{"underOppfolging":true}""")
                )
        )

        stubFor(
            WireMock.get(WireMock.urlPathMatching(".*/mock-ptoproxy/veilarbregistrering/api/arbeidssoker/perioder/niva3.*"))
                .willReturn(
                    okJson("""{"arbeidssokerperioder":[{"fraOgMedDato":"2022-01-01","tilOgMedDato": null}]}""")
                )
        )

        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/arbeidssoker?fraOgMed=2010-01-01") {}.apply {
                assertEquals(HttpStatusCode.OK, this.response.status())
                assertEquals(
                    """{"underoppfolging":{"status":200,"underoppfolging":true},"arbeidssokerperioder":{"status":200,"arbeidssokerperioder":[{"fraOgMedDato":"2022-01-01","tilOgMedDato":null}]}}""",
                    this.response.content
                )
            }
        }
    }
}

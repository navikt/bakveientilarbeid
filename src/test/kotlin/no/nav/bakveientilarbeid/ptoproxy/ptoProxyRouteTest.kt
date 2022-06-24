package no.nav.bakveientilarbeid.ptoproxy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import io.ktor.client.request.*
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

    @Test
    fun `GET oppfolging returnerer 401 når den får det fra ptoproxy`() {
        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/oppfolging") {}.apply {
                assertEquals(HttpStatusCode.Unauthorized, this.response.status())
            }
        }
    }

    @Test
    fun `GET underoppfolging returnerer 500 når den får det fra ptoproxy`() {
        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/underoppfolging") {}.apply {
                assertEquals(HttpStatusCode.InternalServerError, this.response.status())
                assertEquals("uventet feil", this.response.content)
            }
        }
    }

    @Test
    fun `GET arbeidssoker-perioder sender med query parametere`() {
        wireMockServer.stubFor(
            WireMock.post(WireMock.urlPathMatching(".*/mock-ptoproxy/veilarbregistrering/api/arbeidssoker/perioder.*"))
                .withQueryParam("fraOgMed", equalTo("2022-01-01"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody("{\"perioder\":\"[]\"}")
                )
        )

        with(testApplicationEngine) {
            handleRequest(HttpMethod.Get, "/arbeidssoker/perioder?fraOgMed=2022-01-01") {}.apply {
                assertEquals(HttpStatusCode.OK, this.response.status())
                assertEquals("{\"perioder\":\"[]\"}", this.response.content)
            }
        }
    }

    @Test
    fun `POST gjelder-fra sender med request body`() {
        wireMockServer.stubFor(
            WireMock.post(WireMock.urlPathMatching(".*/mock-ptoproxy/veilarbregistrering/api/registrering/gjelderfra.*"))
                .withRequestBody(equalToJson("{\"dato\": \"2022-06-24\"}"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(HttpStatusCode.Created.value)
                )
        )

        with(testApplicationEngine) {
            handleRequest(HttpMethod.Post, "/gjelderfra") {
                request {
                    contentType(ContentType.Application.Json)
                    addHeader(HttpHeaders.ContentType, "application/json")
                    setBody("{\"dato\":\"2022-06-24\"}")
                }
            }.apply {
                assertEquals(HttpStatusCode.Created, this.response.status())
            }
        }
    }

}

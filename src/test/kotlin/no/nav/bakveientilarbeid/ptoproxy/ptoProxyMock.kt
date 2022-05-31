package no.nav.bakveientilarbeid.ptoproxy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

private val ptoProxyTestPath = "/mock-ptoproxy"

internal fun WireMockServer.stubPtoProxyRegistreringGet(): WireMockServer {
    stubFor(
        WireMock.get(WireMock.urlPathMatching(".*$ptoProxyTestPath/veilarbregistrering/api/registrering.*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(204)
            )
    )
    return this
}

internal fun WireMockServer.ptoProxyUrl(): String = baseUrl() + ptoProxyTestPath

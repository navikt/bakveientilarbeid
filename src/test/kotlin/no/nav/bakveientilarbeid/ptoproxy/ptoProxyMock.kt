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

internal fun WireMockServer.stubPtoProxyStartRegistreringGet(): WireMockServer {
    stubFor(
        WireMock.get(WireMock.urlPathMatching(".*$ptoProxyTestPath/veilarbregistrering/api/startregistrering.*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withBody("{\"startregistrering\":\"test\"}")
            )
    )
    return this
}

internal fun WireMockServer.stubPtoProxyOppfolgingGet(): WireMockServer {
    stubFor(
        WireMock.get(WireMock.urlPathMatching(".*$ptoProxyTestPath/veilarboppfolging/api/oppfolging.*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(401)
            )
    )
    return this
}

internal fun WireMockServer.stubPtoProxyUnderOppfolgingGet(): WireMockServer {
    stubFor(
        WireMock.get(WireMock.urlPathMatching(".*$ptoProxyTestPath/veilarboppfolging/api/niva3/underoppfolging.*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(500)
                    .withBody("uventet feil")
            )
    )
    return this
}

internal fun WireMockServer.stubPtoProxyPerioderPost(): WireMockServer {
    stubFor(
        WireMock.post(WireMock.urlPathMatching(".*$ptoProxyTestPath/veilarbregistrering/api/arbeidssoker/perioder.*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withBody("{\"perioder\":\"[]\"}")
            )
    )
    return this
}


internal fun WireMockServer.ptoProxyUrl(): String = baseUrl() + ptoProxyTestPath
internal fun WireMockServer.stubPtoProxy(): WireMockServer = this
    .stubPtoProxyStartRegistreringGet()
    .stubPtoProxyRegistreringGet()
    .stubPtoProxyOppfolgingGet()
    .stubPtoProxyUnderOppfolgingGet()
    .stubPtoProxyPerioderPost()

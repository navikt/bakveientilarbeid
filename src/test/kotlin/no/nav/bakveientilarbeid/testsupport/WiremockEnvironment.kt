package no.nav.bakveientilarbeid.testsupport

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import no.nav.bakveientilarbeid.ptoproxy.stubPtoProxyRegistreringGet


internal class WiremockEnvironment(
    wireMockPort: Int = 8082
) {

    internal val wireMockServer = WireMockBuilder()
        .withPort(wireMockPort)
        .build()
        .stubPtoProxyRegistreringGet()

    internal fun start() = this

    internal fun stop() {
        wireMockServer.stop()
    }

    internal class WireMockBuilder {

        private val config = WireMockConfiguration.options()
        private var port: Int? = null
        private var serverFunction : ((wireMockServer: WireMockServer) -> Unit)? = null
        private var configFunction : ((wireMockConfiguration: WireMockConfiguration) -> Unit)? = null

        fun withPort(port: Int) : WireMockBuilder {
            this.port = port
            return this
        }

        fun build() : WireMockServer {
            if (port == null) config.dynamicPort() else config.port(port!!)

            configFunction?.invoke(config)
            val server = WireMockServer(config)
            serverFunction?.invoke(server)

            server.start()
            WireMock.configureFor(server.port())

            return server
        }
    }
}
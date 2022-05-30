package no.nav.bakveientilarbeid.testsupport


import com.github.tomakehurst.wiremock.WireMockServer
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import no.nav.bakveientilarbeid.config.mainModule
import no.nav.bakveientilarbeid.ptoproxy.ptoProxyUrl
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.util.concurrent.TimeUnit

internal class TestApplicationExtension: ParameterResolver {

    private fun mockEnv() {
        System.setProperty("CORS_ALLOWED_ORIGINS", "localhost")
        System.setProperty("CORS_ALLOWED_SCHEMES", "https")
        System.setProperty("NAIS_CLUSTER_NAME", "dev-fss")
        System.setProperty("NAIS_NAMESPACE", "paw")
        System.setProperty("NAIS_NAMESPACE", "paw")
        System.setProperty("TOKEN_X_WELL_KNOWN_URL", wiremockEnvironment.wireMockServer.baseUrl() + "/tokenx")
        System.setProperty("TOKEN_X_CLIENT_ID", "bakveientilarbeid")
        System.setProperty("TOKEN_X_PRIVATE_JWK", "test123")
        System.setProperty("MELDEKORT_APP_NAME", "meldekort")
        System.setProperty("PTO_PROXY_URL", wiremockEnvironment.wireMockServer.ptoProxyUrl())
    }

    private val testApplicationEngine = TestApplicationEngine(
        environment = createTestEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load().withoutPath("ktor.application.modules"))
            module { mainModule() }
        }
    )

    private val wiremockEnvironment = WiremockEnvironment()

    private val støttedeParametre = listOf(
        TestApplicationEngine::class.java,
        WireMockServer::class.java
    )

    init {
        wiremockEnvironment.start()
        mockEnv()
        testApplicationEngine.start(wait = true)
        Runtime.getRuntime().addShutdownHook(
            Thread {
                testApplicationEngine.stop(10, 60, TimeUnit.SECONDS)
                wiremockEnvironment.stop()
            }
        )
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return støttedeParametre.contains(parameterContext.parameter.type)
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return if (parameterContext.parameter.type == TestApplicationEngine::class.java) testApplicationEngine
        else wiremockEnvironment.wireMockServer
    }
}

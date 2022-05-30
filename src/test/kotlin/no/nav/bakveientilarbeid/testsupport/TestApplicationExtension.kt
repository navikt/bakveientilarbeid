//package no.nav.bakveientilarbeid.testsupport
//
//
//import com.github.tomakehurst.wiremock.WireMockServer
//import com.typesafe.config.ConfigFactory
//import io.ktor.config.*
//import io.ktor.server.engine.*
//import io.ktor.server.testing.*
//import org.junit.jupiter.api.extension.ExtensionContext
//import org.junit.jupiter.api.extension.ParameterContext
//import org.junit.jupiter.api.extension.ParameterResolver
//import java.util.concurrent.TimeUnit
//
//internal class TestApplicationExtension: ParameterResolver {
//
//    private fun mockEnv() {
//        System.setProperty("CORS_ALLOWED_ORIGINS", "localhost")
//        System.setProperty("AZURE_APP_WELL_KNOWN_URL", wiremockEnvironment.wireMockServer.getAzureV2WellKnownUrl())
//        System.setProperty("AZURE_OPENID_CONFIG_TOKEN_ENDPOINT", wiremockEnvironment.wireMockServer.getAzureV2TokenUrl())
//        System.setProperty("AZURE_APP_CLIENT_ID", "paw-proxy")
//        System.setProperty("AZURE_APP_CLIENT_SECRET", "testapp")
//        System.setProperty("NAIS_CLUSTER_NAME", "dev-fss")
//        System.setProperty("NAIS_NAMESPACE", "paw")
//        System.setProperty("HTTP_PROXY", wiremockEnvironment.wireMockServer.baseUrl())
//        System.setProperty("NAIS_NAMESPACE", "paw")
//        System.setProperty("VEILARBREGISTRERING_URL", wiremockEnvironment.wireMockServer.veilarbregistreringUrl())
//        System.setProperty("VEILARBOPPFOLGING_URL", wiremockEnvironment.wireMockServer.veilarboppfolgingUrl())
//        System.setProperty("VEILARBPERSON_URL", wiremockEnvironment.wireMockServer.veilarbpersonUrl())
//        System.setProperty("VEILARBARENA_URL", wiremockEnvironment.wireMockServer.veilarbarenaUrl())
//        System.setProperty("VEILARBVEILEDER_URL", wiremockEnvironment.wireMockServer.veilarbveilederUrl())
//    }
//
//    private val testApplicationEngine = TestApplicationEngine(
//        environment = createTestEnvironment {
//            config = HoconApplicationConfig(ConfigFactory.load().withoutPath("ktor.application.modules"))
//            module { module() }
//        }
//    )
//
//    private val wiremockEnvironment = WiremockEnvironment()
//
//    private val støttedeParametre = listOf(
//        TestApplicationEngine::class.java,
//        WireMockServer::class.java
//    )
//
//    init {
//        wiremockEnvironment.start()
//        mockEnv()
//        testApplicationEngine.start(wait = true)
//        Runtime.getRuntime().addShutdownHook(
//            Thread {
//                testApplicationEngine.stop(10, 60, TimeUnit.SECONDS)
//                wiremockEnvironment.stop()
//            }
//        )
//    }
//
//    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
//        return støttedeParametre.contains(parameterContext.parameter.type)
//    }
//
//    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
//        return if (parameterContext.parameter.type == TestApplicationEngine::class.java) testApplicationEngine
//        else wiremockEnvironment.wireMockServer
//    }
//}
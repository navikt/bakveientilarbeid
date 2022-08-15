package no.nav.bakveientilarbeid.profil

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import no.nav.bakveientilarbeid.config.ApplicationContextLocal
import no.nav.bakveientilarbeid.config.localModule
import no.nav.bakveientilarbeid.ptoproxy.ptoProxyUrl
import no.nav.bakveientilarbeid.testsupport.TestApplicationExtension
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfilRouteTest {
    companion object {
//        private val profilRepositoryImpl = mockk<ProfilRepository>();

        fun mockEnv() {
            System.setProperty("CORS_ALLOWED_ORIGINS", "localhost")
            System.setProperty("CORS_ALLOWED_SCHEMES", "https")
            System.setProperty("NAIS_CLUSTER_NAME", "dev-fss")
            System.setProperty("NAIS_NAMESPACE", "paw")
            System.setProperty("NAIS_NAMESPACE", "paw")
            System.setProperty("TOKEN_X_WELL_KNOWN_URL", "/tokenx")
            System.setProperty("TOKEN_X_CLIENT_ID", "bakveientilarbeid")
            System.setProperty("TOKEN_X_PRIVATE_JWK", "test123")
            System.setProperty("MELDEKORT_APP_NAME", "meldekort")
            System.setProperty("PTO_PROXY_URL", "/pto-proxy")
            System.setProperty("UNLEASH_API_URL", "unleash-api")
        }
    }

    @BeforeEach
    fun initEnv() {
        mockEnv()
    }

    @Test
    fun `GET gir 204 når ingen profil for bruker`()  = testApplication {
        val context = ApplicationContextLocal()

        every {
            context.profilRepositoryImpl.hentProfil(any())
        } returns null

        this.environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        this.application {
            localModule(appContext = context)
        }

        val response = client.get("/profil")

        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}

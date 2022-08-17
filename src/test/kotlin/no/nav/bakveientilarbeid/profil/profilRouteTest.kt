package no.nav.bakveientilarbeid.profil

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.every
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import no.nav.bakveientilarbeid.config.ApplicationContextLocal
import no.nav.bakveientilarbeid.config.localModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfilRouteTest {
    companion object {
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

    @Test
    fun `GET gir 200 med profil for bruker`() = testApplication {
        val context = ApplicationContextLocal()
        val date = LocalDateTime(2022,8, 16, 0, 0, 0)
        val vtaKanReaktiveresVisning = VtaKanReaktiveresVisning(date, true)

        every {
            context.profilRepositoryImpl.hentProfil(any())
        } returns ProfilEntity(
            "1",
            "42",
            ProfilJson(vtaKanReaktiveresVisning),
            date,
            date
        )

        this.environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        this.application {
            localModule(appContext = context)
        }

        val response = client.get("/profil")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(Json.encodeToString(ProfilDto(ProfilJson(vtaKanReaktiveresVisning))), response.bodyAsText())
    }

    @Test
    fun `GET gir 500 når databasen kaster exception`() = testApplication {
        val context = ApplicationContextLocal()

        every {
            context.profilRepositoryImpl.hentProfil(any())
        }.throws(Exception("database error"))

        this.environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        this.application {
            localModule(appContext = context)
        }

        val response = client.get("/profil")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }
}

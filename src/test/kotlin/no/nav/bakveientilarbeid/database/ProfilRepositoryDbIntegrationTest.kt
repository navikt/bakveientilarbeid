package no.nav.bakveientilarbeid.database

import kotlinx.datetime.Instant
import no.nav.bakveientilarbeid.config.Flyway
import no.nav.bakveientilarbeid.profil.Feedback
import no.nav.bakveientilarbeid.profil.JaEllerNei
import no.nav.bakveientilarbeid.profil.ProfilJson
import no.nav.bakveientilarbeid.profil.ProfilRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import javax.sql.DataSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfilRepositoryDbIntegrationTest {
    @Container
    private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:14")

    private lateinit var db: ProfilRepository
    private lateinit var dataSource: DataSource

    private val connection get() = dataSource.connection


    @BeforeAll
    fun beforeAll() {
        postgreSQLContainer.start()
        postgreSQLContainer.withUrlParam("user", postgreSQLContainer.username)
        postgreSQLContainer.withUrlParam("password", postgreSQLContainer.password)

        val postgreSqlDatabase = PostgreSqlDatabase(mapOf(
            "dbUrl" to postgreSQLContainer.jdbcUrl,
            "dbUser" to postgreSQLContainer.username,
            "dbPassword" to postgreSQLContainer.password
        ))

        dataSource = postgreSqlDatabase.dataSource
        db = ProfilRepositoryImpl(postgreSqlDatabase)

        Flyway.configure(dataSource).load().migrate()
    }

    @AfterAll
    fun afterAll() {
        postgreSQLContainer.stop()
    }

    @AfterEach
    fun resetTablesAfterEachTest() {
        connection.use {
            it.prepareStatement("TRUNCATE profil RESTART IDENTITY CASCADE;").execute()
        }
    }

    @Test
    fun `returnerer NULL når bruker_id ikke finnes`() {
        val profil = db.hentProfil("test")
        assertEquals(null, profil)
    }

    @Test
    fun `returnerer nyeste profil for bruker`() {
        val profilJson1 = ProfilJson(aiaFeedbackMeldekortForklaring = Feedback(Instant.parse("2021-08-17T14:15:00.000Z"), "nei"))
        val profilJson2 = ProfilJson(
            aiaFeedbackMeldekortForklaring = Feedback(Instant.parse("2022-08-17T14:15:00.000Z"), "ja"),
            aiaReaktiveringVisning = JaEllerNei("2022-08-17T14:15:00.000Z", "ja")
        )
        db.lagreProfil("42", profilJson1)
        db.lagreProfil("42", profilJson2)

        val profil = db.hentProfil("42")
        assertEquals(profilJson2, profil?.profil)
    }
}

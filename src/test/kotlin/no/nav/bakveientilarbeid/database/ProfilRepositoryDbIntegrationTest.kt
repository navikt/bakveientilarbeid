package no.nav.bakveientilarbeid.database

import kotlinx.datetime.LocalDateTime
import no.nav.bakveientilarbeid.profil.ProfilJson
import no.nav.bakveientilarbeid.profil.Feedback
import org.junit.AfterClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProfilRepositoryDbIntegrationTest {
    val h2Database = H2Database("profil")
    val db = ProfilRepositoryImpl(h2Database)

    @AfterClass
    fun shutDown() {
        h2Database.closeConnection()
    }

    @Test
    fun `returnerer NULL når bruker_id ikke finnes`() {
        val profil = db.hentProfil("test")
        assertEquals(null, profil)
    }

    @Test
    fun `returnerer nyeste profil for bruker`() {
        val profilJson1 = ProfilJson(aiaFeedbackMeldekortForklaring = Feedback(LocalDateTime(2021, 8, 17, 14, 15, 0), true))
        val profilJson2 = ProfilJson(aiaFeedbackMeldekortForklaring = Feedback(LocalDateTime(2022, 8, 17, 14, 15, 0), true))
        db.lagreProfil("42", profilJson1)
        db.lagreProfil("42", profilJson2)

        val profil = db.hentProfil("42")
        assertEquals(profilJson2, profil?.profil)
    }
}

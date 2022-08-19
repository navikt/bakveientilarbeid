package no.nav.bakveientilarbeid.database

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.bakveientilarbeid.profil.ProfilEntity
import no.nav.bakveientilarbeid.profil.ProfilJson
import no.nav.bakveientilarbeid.profil.ProfilRepository
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types

class ProfilRepositoryImpl(val db: Database) : ProfilRepository {
    override fun hentProfil(brukerId: String): ProfilEntity? {
        return runBlocking {
            db.dbQuery { hentProfilQuery(brukerId) }
        }
    }

    override fun lagreProfil(brukerId: String, profil: ProfilJson) {
        runBlocking {
            db.dbQuery { lagreProfilQuery(brukerId, profil) }
        }
    }
}

fun <T> ResultSet.singleResult(result: ResultSet.() -> T?): T? {
    return if (next()) {
        result()
    } else {
        null
    }
}

fun Connection.hentProfilQuery(brukerId: String): ProfilEntity? {
    val sql = "SELECT * from profil where bruker_id=? ORDER BY id DESC LIMIT 1"

    prepareStatement(sql).use {
        it.setString(1, brukerId)
        return it.executeQuery().singleResult {
            ProfilEntity(
                id = getString(1),
                brukerId = getString(2),
                profil = Json.decodeFromString<ProfilJson>(getString(3)),
                createdAt = getTimestamp(4).toInstant().toKotlinInstant(),
            )
        }
    }
}

fun Connection.lagreProfilQuery(brukerId: String, profil: ProfilJson) {
    val sql = "INSERT INTO profil(bruker_id, profil) VALUES(?,?)"
    return prepareStatement(sql)
        .use {
            it.setString(1, brukerId)
            it.setObject(2, Json.encodeToString(profil), Types.BLOB)
            it.executeUpdate()
        }
}

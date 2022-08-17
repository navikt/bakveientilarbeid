package no.nav.bakveientilarbeid.profil

import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class VtaKanReaktiveresVisning(@Contextual val updated: LocalDateTime, val  state: Boolean)

@Serializable
data class ProfilJson(
    val vtaKanReaktiveresVisning: VtaKanReaktiveresVisning?
)

data class ProfilEntity(
    val id: String,
    val brukerId: String,
    val profil: ProfilJson,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

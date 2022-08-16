package no.nav.bakveientilarbeid.profil

import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDateTime

@kotlinx.serialization.Serializable
data class VtaKanReaktiveresVisning(@Contextual val updated: LocalDateTime, val  state: Boolean)

@kotlinx.serialization.Serializable
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

package no.nav.bakveientilarbeid.profil

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ProfilDto(@Contextual val profil: ProfilJson) {
    companion object {
        fun fra(profilEntity: ProfilEntity): ProfilDto {
            return ProfilDto(profilEntity.profil)
        }
    }
}


